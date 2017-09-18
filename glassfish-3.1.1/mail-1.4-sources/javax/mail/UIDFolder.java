/*
 * The contents of this file are subject to the terms 
 * of the Common Development and Distribution License 
 * (the "License").  You may not use this file except 
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt or 
 * https://glassfish.dev.java.net/public/CDDLv1.0.html. 
 * See the License for the specific language governing 
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL 
 * HEADER in each file and include the License file at 
 * glassfish/bootstrap/legal/CDDLv1.0.txt.  If applicable, 
 * add the following below this CDDL HEADER, with the 
 * fields enclosed by brackets "[]" replaced with your 
 * own identifying information: Portions Copyright [yyyy] 
 * [name of copyright owner]
 */

/*
 * @(#)UIDFolder.java	1.11 05/08/29
 *
 * Copyright 1997-2005 Sun Microsystems, Inc. All Rights Reserved.
 */

package javax.mail;

import java.util.NoSuchElementException;

/**
 * The <code>UIDFolder</code> interface is implemented by Folders 
 * that can support the "disconnected" mode of operation, by providing 
 * unique-ids for messages in the folder. This interface is based on
 * the IMAP model for supporting disconnected operation. <p>
 *
 * A Unique identifier (UID) is a positive long value, assigned to
 * each message in a specific folder. Unique identifiers are assigned
 * in a strictly <strong>ascending</strong> fashion in the mailbox. 
 * That is, as each message is added to the mailbox it is assigned a 
 * higher UID than the message(s) which were added previously. Unique
 * identifiers persist across sessions. This permits a client to 
 * resynchronize its state from a previous session with the server. <p>
 *
 * Associated with every mailbox is a unique identifier validity value.
 * If unique identifiers from an earlier session fail to persist to 
 * this session, the unique identifier validity value 
 * <strong>must</strong> be greater than the one used in the earlier
 * session. <p>
 *
 * Refer to RFC 2060 <A HREF="http://www.ietf.org/rfc/rfc2060.txt">
 * http://www.ietf.org/rfc/rfc2060.txt</A> for more information.
 *
 * @author John Mani
 */

public interface UIDFolder {

    /**
     * A fetch profile item for fetching UIDs.
     * This inner class extends the <code>FetchProfile.Item</code>
     * class to add new FetchProfile item types, specific to UIDFolders.
     * The only item currently defined here is the <code>UID</code> item.
     *
     * @see FetchProfile
     */
    public static class FetchProfileItem extends FetchProfile.Item {
	protected FetchProfileItem(String name) {
	    super(name);
	}

	/**
	 * UID is a fetch profile item that can be included in a
	 * <code>FetchProfile</code> during a fetch request to a Folder.
	 * This item indicates that the UIDs for messages in the specified 
	 * range are desired to be prefetched. <p>
	 * 
	 * An example of how a client uses this is below: <p>
	 * <blockquote><pre>
	 *
	 * 	FetchProfile fp = new FetchProfile();
	 *	fp.add(UIDFolder.FetchProfileItem.UID);
	 *	folder.fetch(msgs, fp);
	 *
	 * </pre></blockquote><p>
	 */ 
	public static final FetchProfileItem UID = 
		new FetchProfileItem("UID");
    }

    /**
     * This is a special value that can be used as the <code>end</code>
     * parameter in <code>getMessages(start, end)</code>, to denote the
     * last UID in this folder.
     *
     * @see #getMessagesByUID
     */ 
    public final static long LASTUID = -1;

    /**
     * Returns the UIDValidity value associated with this folder. <p>
     * 
     * Clients typically compare this value against a UIDValidity
     * value saved from a previous session to insure that any cached 
     * UIDs are not stale.
     *
     * @return UIDValidity
     */
    public long getUIDValidity() throws MessagingException;

    /**
     * Get the Message corresponding to the given UID. If no such 
     * message exists, <code>null</code> is returned.
     *
     * @param uid	UID for the desired message
     * @return		the Message object. <code>null</code> is returned
     *			if no message corresponding to this UID is obtained.
     * @exception	MessagingException
     */
    public Message getMessageByUID(long uid) throws MessagingException;

    /**
     * Get the Messages specified by the given range. The special
     * value LASTUID can be used for the <code>end</code> parameter
     * to indicate the last available UID. 
     *
     * @param start	start UID
     * @param end	end UID
     * @return		array of Message objects
     * @exception	MessagingException
     * @see 		#LASTUID
     */
    public Message[] getMessagesByUID(long start, long end)
				throws MessagingException;

    /**
     * Get the Messages specified by the given array of UIDs. If any UID is 
     * invalid, <code>null</code> is returned for that entry. <p>
     *
     * Note that the returned array will be of the same size as the specified
     * array of UIDs, and <code>null</code> entries may be present in the
     * array to indicate invalid UIDs.
     *
     * @param uids	array of UIDs
     * @return		array of Message objects
     * @exception	MessagingException
     */
    public Message[] getMessagesByUID(long[] uids) 
				throws MessagingException;

    /**
     * Get the UID for the specified message. Note that the message
     * <strong>must</strong> belong to this folder. Otherwise
     * java.util.NoSuchElementException is thrown.
     *
     * @param message	Message from this folder
     * @return		UID for this message
     * @exception	NoSuchElementException if the given Message
     *			is not in this Folder.
     */
    public long getUID(Message message) throws MessagingException;
}
