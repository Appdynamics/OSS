package flex.messaging;

/**
 * Defines the lifecycle interface for FlexComponents, allowing
 * the server to manage the running state of server components
 * through a consistent interface.
 */
public interface FlexComponent extends FlexConfigurable
{
    /**
     * Invoked to start the component.
     * The {@link FlexConfigurable#initialize(String, flex.messaging.config.ConfigMap)} method inherited 
     * from the {@link FlexConfigurable} interface must be invoked before this method is invoked.
     * Once this method returns, {@link #isStarted()} must return true.
     */
    void start();

    /**
     * Invoked to stop the component.
     * Once this method returns, {@link #isStarted()} must return false.
     */
    void stop();

    /**
     * Indicates whether the component is started and running.
     * 
     * @return <code>true</code> if the component has started; 
     *         otherwise <code>false</code>.
     */
    boolean isStarted();   
}
