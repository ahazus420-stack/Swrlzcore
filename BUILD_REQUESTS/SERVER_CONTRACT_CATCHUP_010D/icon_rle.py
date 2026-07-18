from __future__ import annotations

import json
import struct
import zlib
from pathlib import Path


def load(asset_dir: Path) -> tuple[int, int, list[tuple[int, int, int]], list[int]]:
    document = json.loads((asset_dir / "icon_meta.json").read_text(encoding="utf-8"))
    if document.get("format") != "SWRLZ-RLE-ICON-v1":
        raise RuntimeError("unsupported launcher icon transport")
    rows = []
    for part in sorted(asset_dir.glob("rows-*.json")):
        rows.extend(json.loads(part.read_text(encoding="utf-8")))
    width, height = int(document["width"]), int(document["height"])
    palette = [tuple(int(color[index:index + 2], 16) for index in (1, 3, 5)) for color in document["palette"]]
    pixels: list[int] = []
    for row in rows:
        expanded: list[int] = []
        for count, palette_index in row:
            expanded.extend([int(palette_index)] * int(count))
        if len(expanded) != width:
            raise RuntimeError(f"launcher icon row width mismatch: {len(expanded)} != {width}")
        pixels.extend(expanded)
    if len(rows) != height or len(pixels) != width * height:
        raise RuntimeError("launcher icon pixel count mismatch")
    return width, height, palette, pixels


def scale(width: int, height: int, pixels: list[int], target_width: int, target_height: int) -> list[int]:
    output: list[int] = []
    for y in range(target_height):
        source_y = min(height - 1, y * height // target_height)
        row_offset = source_y * width
        for x in range(target_width):
            source_x = min(width - 1, x * width // target_width)
            output.append(pixels[row_offset + source_x])
    return output


def _chunk(kind: bytes, payload: bytes) -> bytes:
    checksum = zlib.crc32(kind + payload) & 0xFFFFFFFF
    return struct.pack(">I", len(payload)) + kind + payload + struct.pack(">I", checksum)


def write_png(path: Path, width: int, height: int, palette: list[tuple[int, int, int]], pixels: list[int]) -> None:
    if len(palette) > 256:
        raise RuntimeError("indexed PNG palette exceeds 256 colors")
    palette_bytes = b"".join(bytes(color) for color in palette)
    palette_bytes += b"\x00" * (768 - len(palette_bytes))
    scanlines = bytearray()
    for y in range(height):
        scanlines.append(0)
        start = y * width
        scanlines.extend(pixels[start:start + width])
    png = b"\x89PNG\r\n\x1a\n"
    png += _chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 3, 0, 0, 0))
    png += _chunk(b"PLTE", palette_bytes)
    png += _chunk(b"IDAT", zlib.compress(bytes(scanlines), 9))
    png += _chunk(b"IEND", b"")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)
