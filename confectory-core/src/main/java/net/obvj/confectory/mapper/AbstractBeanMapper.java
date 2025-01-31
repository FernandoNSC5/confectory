package net.obvj.confectory.mapper;

import java.io.InputStream;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.BeanConfigurationHelper;

/**
 * An abstract {@code Mapper} for implementations intended to load the contents of an
 * {@link InputStream} into user-defined beans.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractBeanMapper<T> implements Mapper<T>
{

    /**
     * @return a {@link BeanConfigurationHelper}, since the configuration for this type of
     *         {@code Mapper} is intended to be retrieved by the user-defined bean.
     */
    @Override
    public ConfigurationHelper<T> configurationHelper(T bean)
    {
        return new BeanConfigurationHelper<>(bean);
    }

}
