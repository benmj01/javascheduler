package ru.scheduler;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 27.11.2009
 * Time: 18:26:08
 */

@XmlRegistry
public abstract class ObjectFactory {
    public abstract Configuration createConfiguration();
}
