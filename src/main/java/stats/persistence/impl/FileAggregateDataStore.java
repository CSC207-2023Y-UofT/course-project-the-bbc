package stats.persistence.impl;

import stats.entry.StatEntry;
import stats.persistence.StatAggregateDataStore;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.file.Files;
import java.util.*;

/**
 * A file-based implementation of the {@link StatAggregateDataStore} interface.
 * This class provides methods to store and retrieve aggregated statistic data from a file system.
 */
public class FileAggregateDataStore implements StatAggregateDataStore {

    /**
     * The directory where the aggregated data files are stored.
     */
    private final File directory;

    private final int pageSize = 256;

    /**
     * Constructs a FileAggregateDataStore instance with a specified directory.
     *
     * @param directory The directory where the aggregated data files are stored.
     */
    public FileAggregateDataStore(File directory) {
        directory.mkdirs();
        this.directory = directory;
    }

    /**
     * Returns a file instance pointing to the data file for the given page, entry class, and aggregate class.
     *
     * @param page           The page of the data.
     * @param entryClass     The entry class of the data.
     * @param aggregateClass The aggregate class of the data.
     * @return A File instance.
     */
    private File getFile(long page, Class<? extends StatEntry> entryClass, Class<?> aggregateClass) {
        return new File(directory, entryClass.getSimpleName() + "-" + aggregateClass.getSimpleName() + "-" + page);
    }

    /**
     * Read a page of data from the file system.
     *
     * @param pageNumber     The page number to read.
     * @param entryClass     The entry class.
     * @param aggregateClass The aggregate class.
     * @param <E>            The entry class.
     * @param <A>            The aggregate class.
     * @return The map of data read from the file.
     */
    private synchronized <E extends StatEntry, A> Map<Long, A> read(long pageNumber, Class<E> entryClass, Class<A> aggregateClass) {
        File file = getFile(pageNumber, entryClass, aggregateClass);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try {
            byte[] bytes = Files.readAllBytes(file.toPath());

            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            // Get the number of elements
            int numElements = buffer.getInt();

            // Read the keys
            long[] keys = new long[numElements];
            LongBuffer keyBuffer = buffer.asLongBuffer();
            keyBuffer.get(keys);

            // Now we get the rest of the buffer as a byte array
            buffer.position(buffer.position() + numElements * 8);
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            // And parse it into the map
            ByteArrayInputStream inStream = new ByteArrayInputStream(data);
            try (ObjectInputStream ois = new ObjectInputStream(inStream)) {
                Map<Long, A> map = new HashMap<>();
                for (long key : keys) {
                    map.put(key, aggregateClass.cast(ois.readObject()));
                }
                return map;
            }

        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    /**
     * Write a page of data to the file system.
     *
     * @param pageNumber     The page number to write.
     * @param entryClass     The entry class.
     * @param aggregateClass The aggregate class.
     * @param map            The map of data to write.
     * @param <E>            The entry class.
     * @param <A>            The aggregate class.
     */
    private <E extends StatEntry, A> void write(long pageNumber, Class<E> entryClass, Class<A> aggregateClass, Map<Long, A> map) {
        File file = getFile(pageNumber, entryClass, aggregateClass);

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(outStream)) {
                for (Map.Entry<Long, A> entry : map.entrySet()) {
                    oos.writeObject(entry.getValue());
                }
            }

            byte[] entryData = outStream.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(4 + 8 * map.size() + entryData.length);

            // Write the number of elements
            buffer.putInt(map.size());

            // Write the keys
            LongBuffer keyBuffer = buffer.asLongBuffer();
            for (long key : map.keySet()) {
                keyBuffer.put(key);
            }

            // Write the data
            buffer.position(buffer.position() + map.size() * 8);
            buffer.put(entryData);

            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);

            Files.write(file.toPath(), bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long toPage(long index) {
        return index / pageSize;
    }

    /**
     * Retrieves the aggregate value from a file for the given index, entry class, and aggregate class.
     * If no data is found, it returns an empty Optional.
     *
     * @param startIndex     The start index of the data.
     * @param endIndex       The end index of the data, inclusive.
     * @param entryClass     The entry class of the data.
     * @param aggregateClass The aggregate class of the data.
     * @param <E>            The entry class.
     * @param <A>            The aggregate class.
     * @return An Optional containing the aggregate value if found, or an empty Optional if not found.
     */
    @Override
    public synchronized <E extends StatEntry, A> Map<Long, A> retrieve(long startIndex, long endIndex, Class<E> entryClass, Class<A> aggregateClass) {
        long startPage = toPage(startIndex);
        long endPage = toPage(endIndex);

        Map<Long, A> map = new HashMap<>();

        for (long page = startPage; page <= endPage; page++) {
            Map<Long, A> pageMap = read(page, entryClass, aggregateClass);
            for (long index : pageMap.keySet()) {
                if (index >= startIndex && index <= endIndex) {
                    map.put(index, pageMap.get(index));
                }
            }
        }

        return map;
    }

    /**
     * Stores the given aggregate value into a file for the given index, entry class, and aggregate class.
     *
     * @param index          The index of the data.
     * @param entryClass     The entry class of the data.
     * @param aggregateClass The aggregate class of the data.
     * @param aggregate      The aggregate value to store.
     * @param <E>            The entry class.
     * @param <A>            The aggregate class.
     */
    @Override
    public synchronized <E extends StatEntry, A> void store(long index, Class<E> entryClass, Class<A> aggregateClass, A aggregate) {
        long page = toPage(index);
        Map<Long, A> map = read(page, entryClass, aggregateClass);
        map.put(index, aggregate);
        write(page, entryClass, aggregateClass, map);
    }

}
