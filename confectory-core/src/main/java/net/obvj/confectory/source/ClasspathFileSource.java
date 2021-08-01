package net.obvj.confectory.source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.obvj.performetrics.Counter;
import net.obvj.performetrics.Stopwatch;
import net.obvj.performetrics.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A specialized configuration source implementation for loading a local file resource
 * from the classpath.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class ClasspathFileSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathFileSource.class);

    /**
     * Builds a new configuration source for specific local file resource in the classpath.
     *
     * @param path the classpath file name (or relative path) for this configuration source.
     */
    public ClasspathFileSource(String path)
    {
        super(path);
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        LOGGER.info("Searching file: {}", super.parameter);

        URL url = ClasspathFileSource.class.getClassLoader().getResource(super.parameter);
        if (url == null)
        {
            String message = String.format("Classpath file not found: %s", super.parameter);
            LOGGER.warn(message);
            throw new ConfigurationSourceException(message);
        }

        return load(url, mapper);
    }

    /**
     * Gets the contents of the specified URL.
     *
     * @param url    the URL to be loaded
     * @param mapper the {@link Mapper} to be applied on the file input stream
     * @return the string content from the specified URL
     */
    protected T load(URL url, Mapper<T> mapper)
    {
        try (InputStream inputStream = url.openStream())
        {
            LOGGER.debug("Loading file {} with mapper: <{}>", super.parameter, mapper.getClass().getSimpleName());

            Stopwatch stopwatch = Stopwatch.createStarted(Counter.Type.WALL_CLOCK_TIME);
            T mappedObject = mapper.apply(inputStream);
            stopwatch.stop();
            Duration elapsedTime = stopwatch.elapsedTime(Counter.Type.WALL_CLOCK_TIME);

            LOGGER.info("File {} loaded successfully", super.parameter);
            LOGGER.info("File loaded in {}", elapsedTime);
            return mappedObject;
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load classpath resource: %s", super.parameter);
        }
    }

}
