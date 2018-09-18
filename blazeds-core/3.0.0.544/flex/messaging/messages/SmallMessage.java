package flex.messaging.messages;

/**
 * A marker interface that is used to indicate that a Message has an
 * alternative smaller form for serialization.
 * @exclude
 */
public interface SmallMessage extends Message
{
    /**
     * This method must be implemented by subclasses that have an
     * <code>java.io.Externalizable</code> or "small" form, or null to
     * indicate that a small form is not available.
     *
     * @return An alternative representation of a
     * flex.messaging.messages.Message so that the size of the serialized
     * message is smaller.
     */
    Message getSmallMessage();
}
