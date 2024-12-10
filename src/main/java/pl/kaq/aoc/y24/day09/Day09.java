package pl.kaq.aoc.y24.day09;

import pl.kaq.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.util.Collections.swap;

record File(long id, long size) implements Block {
    @Override
    public Block reduce(long reduceSize) {
        return new File(id, size - reduceSize);
    }

    @Override
    public Block part(long partSize) {
        return new File(id, partSize);
    }

    public long sum(long num) {
        return LongStream.range(num, num + size)
                .map(idx -> idx * id)
                .sum();
    }
}

record FreeSpace(long size) implements Block {

    @Override
    public Block reduce(long reduceSize) {
        return new FreeSpace(size - reduceSize);
    }

    @Override
    public Block part(long partSize) {
        return new FreeSpace(partSize);
    }

    @Override
    public long sum(long num) {
        return 0;
    }
}

sealed interface Block permits File, FreeSpace {
    long size();

    Block reduce(long size);

    Block part(long partSize);

    long sum(long num);
}

public class Day09 extends Solution {
    @Override
    public String firstStar(String input) {
        var blocks = parseBlocks(input);
        var compactedBlocks = compact(blocks);
        return Long.toString(calculateChecksum(compactedBlocks));
    }

    private List<Block> compact(List<Block> blocks) {
        var mutableBlocks = new ArrayList<>(blocks);

        while (true) {
            var freeIdx = findFreeSpaceIdx(mutableBlocks);
            var freeSpace = mutableBlocks.get(freeIdx);

            var fileIdx = findFileIdx(mutableBlocks);
            var file = mutableBlocks.get(fileIdx);

            if (freeIdx > fileIdx) {
                break;
            }

            mutableBlocks.remove(fileIdx);
            mutableBlocks.remove(freeIdx);
            if (freeIdx + 1 == fileIdx) {
                mutableBlocks.add(freeIdx, file);
                mutableBlocks.add(freeIdx + 1, freeSpace);
                compactLastTwo(mutableBlocks);
                compactLastTwo(mutableBlocks);
                continue;
            }
            if (freeSpace.size() > file.size()) {
                mutableBlocks.add(freeIdx, file);
                mutableBlocks.add(freeIdx + 1, freeSpace.reduce(file.size()));
                mutableBlocks.add(freeSpace.part(file.size()));
                compactLastTwo(mutableBlocks);
                compactLastTwo(mutableBlocks);
                continue;
            }
            if (freeSpace.size() == file.size()) {
                mutableBlocks.add(freeIdx, file);
                mutableBlocks.add(freeSpace);
                compactLastTwo(mutableBlocks);
                compactLastTwo(mutableBlocks);
                continue;
            }
            if (freeSpace.size() < file.size()) {
                mutableBlocks.add(freeIdx, file.part(freeSpace.size()));
                mutableBlocks.add(fileIdx, file.reduce(freeSpace.size()));
                mutableBlocks.add(freeSpace);
                compactLastTwo(mutableBlocks);
                compactLastTwo(mutableBlocks);
            }
        }
        return mutableBlocks;
    }

    private void compactLastTwo(ArrayList<Block> mutableBlocks) {
        var last = mutableBlocks.removeLast();
        var prev = mutableBlocks.removeLast();
        if (last instanceof FreeSpace(long lastSize) && prev instanceof FreeSpace(long prevSize)) {
            mutableBlocks.add(new FreeSpace(lastSize + prevSize));
            return;
        }
        if (last instanceof File(long lastId, long lastSize) && prev instanceof File(long prevId, long prevSize)) {
            if (lastId == prevId) {
                mutableBlocks.add(new File(lastId, lastSize + prevSize));
                return;
            }
        }
        mutableBlocks.add(prev);
        mutableBlocks.add(last);
    }

    private int findFileIdx(List<Block> blocks) {
        for (var i = blocks.size() - 1; i > 0; i--) {
            if (blocks.get(i) instanceof File) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private int findFreeSpaceIdx(List<Block> blocks) {
        for (var i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) instanceof FreeSpace) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    private Long calculateChecksum(List<Block> blocks) {
        var num = 0L;
        var sum = 0L;
        for (var block : blocks) {
            sum += block.sum(num);
            num += block.size();
        }
        return sum;
    }

    private List<Block> parseBlocks(String input) {
        var blocks = Arrays.stream(input.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();
        return IntStream.range(0, blocks.length)
                .mapToObj((IntFunction<Block>) i -> i % 2 == 0 ? new File(i / 2, blocks[i]) : new FreeSpace(blocks[i]))
                .toList();
    }

    @Override
    public String secondStar(String input) {
        var blocks = parseBlocks(input);
        var compactedBlocks = compactMovingFullFiles(blocks);
        return Long.toString(calculateChecksum(compactedBlocks));
    }

    private List<Block> compactMovingFullFiles(List<Block> blocks) {
        var mutableBlocks = new ArrayList<>(blocks);
        var lastFileId = findLastFileId(mutableBlocks);
        while (true) {
            var fileIdx = findFileIdx(mutableBlocks, lastFileId);
            if (fileIdx < 0) {
                break;
            }
            var file = mutableBlocks.get(fileIdx);
            var freeSpaceIdxOptional = findFreeSpaceIdx(mutableBlocks, file.size());
            if (freeSpaceIdxOptional.isEmpty()) {
                lastFileId -= 1;
                continue;
            }
            var freeSpaceIdx = (int) freeSpaceIdxOptional.get();
            if (freeSpaceIdx > fileIdx) {
                lastFileId -= 1;
                continue;
            }
            var freeSpace = mutableBlocks.get(freeSpaceIdx);


            if (freeSpace.size() == file.size()) {
                swap(mutableBlocks, fileIdx, freeSpaceIdx);
            } else {
                mutableBlocks.remove(fileIdx);
                mutableBlocks.add(fileIdx, new FreeSpace(file.size()));
                mutableBlocks.remove(freeSpaceIdx);
                mutableBlocks.add(freeSpaceIdx, file);
                mutableBlocks.add(freeSpaceIdx + 1, freeSpace.reduce(file.size()));
            }
            lastFileId -= 1;
        }

        return mutableBlocks;
    }

    private Optional<Integer> findFreeSpaceIdx(List<Block> blocks, long size) {
        for (var idx = 0; idx < blocks.size(); idx++) {
            if (blocks.get(idx) instanceof FreeSpace(long space) && space >= size) {
                return Optional.of(idx);
            }
        }
        return Optional.empty();
    }

    private int findFileIdx(List<Block> blocks, long id) {
        for (var idx = blocks.size() - 1; idx >= 0; idx--) {
            if (blocks.get(idx) instanceof File f && f.id() == id) {
                return idx;
            }
        }
        return -1;
    }

    private long findLastFileId(List<Block> blocks) {
        for (var idx = blocks.size() - 1; idx >= 0; idx--) {
            if (blocks.get(idx) instanceof File f) {
                return f.id();
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        new Day09().run("input.in");
    }
}