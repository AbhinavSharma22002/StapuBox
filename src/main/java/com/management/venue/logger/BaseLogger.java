package com.management.venue.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseLogger {
    // This logger automatically uses the name of the class that extends it
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
}