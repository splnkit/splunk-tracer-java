package com.splunk.tracer.jre;

import com.splunk.tracer.shared.AbstractTracer;
import com.splunk.tracer.shared.Options;
import com.splunk.tracer.shared.SimpleFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.splunk.tracer.jre.Version.SPLUNK_TRACER_VERSION;

public class JRETracer extends AbstractTracer {
    private static final int JRE_DEFAULT_REPORTING_INTERVAL_MILLIS = 2500;

    private static final Logger LOGGER = LoggerFactory.getLogger(JRETracer.class);

    private static class JavaTracerHolder {
        private static final JRETracer INSTANCE = new JRETracer(null);
    }

    /**
     * Returns the singleton Tracer instance that can be utilized to record logs and spans.
     *
     * @return tracer instance
     */
    @SuppressWarnings("unused")
    public static JRETracer getInstance() {
        return JavaTracerHolder.INSTANCE;
    }

    public JRETracer(Options options) {
        super(options.setDefaultReportingIntervalMillis(JRE_DEFAULT_REPORTING_INTERVAL_MILLIS));
        addStandardTracerTags();
    }

    // Flush any data stored in the log and span buffers
    protected SimpleFuture<Boolean> flushInternal(boolean explicitRequest) {
        return new SimpleFuture<>(sendReport(explicitRequest));
    }

    protected void printLogToConsole(InternalLogLevel level, String msg, Object payload) {
        String s = msg;
        if (payload != null) {
            s += " " + payload.toString();
        }
        switch (level) {
            case DEBUG:
                LOGGER.debug(s);
                break;
            case INFO:
                LOGGER.info(s);
                break;
            case WARN:
                LOGGER.warn(s);
                break;
            case ERROR:
                LOGGER.error(s);
                break;
        }
    }

    /**
     * Adds standard tags set by all SplunkTracing client libraries.
     */
    private void addStandardTracerTags() {
        // The platform is called "jre" rather than "Java" to clearly
        // differentiate this library from the Android version
        addTracerTag(SPLUNK_TRACER_PLATFORM_KEY, "jre");
        addTracerTag(SPLUNK_TRACER_PLATFORM_VERSION_KEY, System.getProperty("java.version"));
        addTracerTag(SPLUNK_TRACER_VERSION_KEY, SPLUNK_TRACER_VERSION);
    }
}
