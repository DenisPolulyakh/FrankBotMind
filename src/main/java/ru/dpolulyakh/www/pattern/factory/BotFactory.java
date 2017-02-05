package ru.dpolulyakh.www.pattern.factory;

import java.io.IOException;

/**
 * @author Denis Polulyakh
 *         24.01.2017.
 */
public abstract class BotFactory {
    public abstract Processor getProcessor(String inputJSONMessage) throws IOException;
}
