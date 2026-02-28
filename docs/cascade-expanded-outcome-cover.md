# Cascade-Expanded Outcome Cover (CEOC)

## Overview

The Cascade-Expanded Outcome Cover (CEOC) is a deterministic enumeration strategy
for generating betting slips (or scenario combinations) from a discrete outcome space
while avoiding full cartesian explosion.

Instead of expanding all outcomes uniformly, CEOC expands matchDtos left-to-right
and continues expansion only along a designated *dominant* outcome.

This guarantees full coverage of the outcome space while producing significantly
fewer slips than naive cartesian enumeration.

---

## Outcome Model

Each match has an ordered set of outcomes:

- **F** — Favorable (terminates expansion)
- **E** — Neutral (terminates expansion)
- **D** — Dominant (continues expansion)

The ordering expresses priority, not probability.

---

## Block Types

Slip generation is configured using ordered blockDtos.

### FULL Block

- Enumerates **all outcomes** for each match in the block
- Equivalent to a cartesian product
- Only the **all-D combination** is allowed to continue into the next block
- All other combinations terminate and emit a slip

### CASCADE Block

- Matches are processed sequentially
- For each match:
    - **F or E** → emit slip and stop
    - **D** → continue to next match
- If all matchDtos in the block are D:
    - expansion continues to the next block
    - or emits a final slip if this is the last block

---

## Example

Block configuration:


This produces:
- Early termination for non-dominant outcomes
- Deep expansion only on the critical all-D path
- A predictable, bounded number of slips

---

## Properties

- Full outcome coverage
- Deterministic output
- Configurable depth of expansion
- Suitable for risk-controlled hedging strategies

---

## Use Cases

- Betting slip reduction
- Scenario stress testing
- Decision-tree compression
- Portfolio outcome coverage