/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.ejb.containers;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.io.IOException;
import java.util.Date;

import com.sun.ejb.EJBUtils;

/**
 * TimerState represents the state of a persistent EJB Timer.  
 * It is part of the EJB container and is implemented as an Entity.
 *
 * @author Marina Vatkina
 */
@Entity(name="Timer")
@NamedQueries({
    @NamedQuery(
        name="findTimerIdsByContainer",
        query="SELECT t.timerId FROM Timer t WHERE t.containerId = ?1"
    ),
    @NamedQuery(
        name="findTimerIdsByContainerAndState",
        query="SELECT t.timerId FROM Timer t WHERE t.containerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="findTimerIdsByContainerAndOwner",
        query="SELECT t.timerId FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2"
    ),
    @NamedQuery(
        name="findTimerIdsByContainerAndOwnerAndState",
        query="SELECT t.timerId FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2 AND t.state=?3"
    ),
    @NamedQuery(
        name="findTimerIdsByOwner",
        query="SELECT t.timerId FROM Timer t WHERE t.ownerId = ?1"
    ),
    @NamedQuery(
        name="findTimerIdsByOwnerAndState",
        query="SELECT t.timerId FROM Timer t WHERE t.ownerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="findTimersByContainer",
        query="SELECT t FROM Timer t WHERE t.containerId = ?1"
    ),
    @NamedQuery(
        name="findTimersByContainerAndState",
        query="SELECT t FROM Timer t WHERE t.containerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="findTimersByContainerAndOwner",
        query="SELECT t FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2"
    ),
    @NamedQuery(
        name="findTimersByContainerAndOwnerAndState",
        query="SELECT t FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2 AND t.state=?3"
    ),
    @NamedQuery(
        // Used also for timer migration, so needs to have predictable return order
        name="findTimersByOwner",
        query="SELECT t FROM Timer t WHERE t.ownerId = ?1 ORDER BY t.timerId"
    ),
    @NamedQuery(
        name="findTimersByOwnerAndState",
        query="SELECT t FROM Timer t WHERE t.ownerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="countTimersByOwner",
        query="SELECT COUNT(t) FROM Timer t WHERE t.ownerId = ?1"
    ),
    @NamedQuery(
        name="countTimersByOwnerAndState",
        query="SELECT COUNT(t) FROM Timer t WHERE t.ownerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="countTimersByContainer",
        query="SELECT COUNT(t) FROM Timer t WHERE t.containerId = ?1"
    ),
    @NamedQuery(
        name="countTimersByContainerAndState",
        query="SELECT COUNT(t) FROM Timer t WHERE t.containerId = ?1 AND t.state=?2"
    ),
    @NamedQuery(
        name="countTimersByContainerAndOwner",
        query="SELECT COUNT(t) FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2"
    ),
    @NamedQuery(
        name="countTimersByContainerAndOwnerAndState",
        query="SELECT COUNT(t) FROM Timer t WHERE t.containerId = ?1 AND t.ownerId=?2 AND t.state=?3"
    )
/**
    ,
    @NamedQuery(
        name="updateTimersFromOwnerToNewOwner",
        query="UPDATE Timer t SET t.ownerId = :toOwner WHERE t.ownerId = :fromOwner"
    )
**/
})
@Table(name="EJB__TIMER__TBL")
@IdClass(com.sun.ejb.containers.TimerPrimaryKey.class)
public class TimerState implements Serializable {

    // Timer states
    public static final int ACTIVE    = 0;
    public static final int CANCELLED = 1;

    //
    // Persistence fields and access methods
    //

    @Id
    @Column(name="TIMERID")
    private String timerId;

    @Column(name="CREATIONTIMERAW")
    private long creationTimeRaw;

    @Column(name="INITIALEXPIRATIONRAW")
    private long initialExpirationRaw;

    @Column(name="LASTEXPIRATIONRAW")
    private long lastExpirationRaw;

    @Column(name="INTERVALDURATION")
    private long intervalDuration;

    @Column(name="STATE")
    private int state;

    @Column(name="CONTAINERID")
    private long containerId;

    @Column(name="PKHASHCODE")
    private int pkHashCode;

    @Column(name="OWNERID")
    private String ownerId;

    @Column(name="SCHEDULE")
    private String schedule;

    @Lob 
    @Basic(fetch=FetchType.LAZY)
    @Column(name="BLOB")
    private Blob blob;

    // primary key
    public String getTimerId() {
        return timerId;
    }

    public void setTimerId(String timerId) {
        this.timerId = timerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public long getCreationTimeRaw() {
        return creationTimeRaw;
    }

    public void setCreationTimeRaw(long creationTime) {
        creationTimeRaw = creationTime;
    }

    public long getInitialExpirationRaw() {
        return initialExpirationRaw;
    }

    public void setInitialExpirationRaw(long initialExpiration) {
        initialExpirationRaw = initialExpiration;
    }

    public long getLastExpirationRaw() {
        return lastExpirationRaw;
    }

    public void setLastExpirationRaw(long lastExpiration) {
        lastExpirationRaw = lastExpiration;
    }

    public long getIntervalDuration() {
        return intervalDuration;
    }

    public void setIntervalDuration(long intervalDuration) {
        this.intervalDuration = intervalDuration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Blob getBlob() {
        return blob;
    }

    public void setBlob(Blob blob) {
        this.blob = blob;
    }

    public int getPkHashCode() {
        return pkHashCode;
    }

    public void setPkHashCode(int pkHash) {
        pkHashCode = pkHash;
    }

    public TimerSchedule getTimerSchedule() {
        return timerSchedule_;
    }

    //
    // These data members contain derived state for 
    // some immutable fields.
    //

    // deserialized state from blob
    @Transient
    private boolean blobLoaded_;

    private transient Object timedObjectPrimaryKey_;
    private transient Serializable info_;

    // Dates
    private transient Date creationTime_;
    private transient Date initialExpiration_;
    private transient Date lastExpiration_;
    private transient TimerSchedule timerSchedule_;
    
    public TimerState () {
    }

    public TimerState (String timerId, long containerId, String ownerId,
             Object timedObjectPrimaryKey, 
             Date initialExpiration, long intervalDuration, 
             TimerSchedule schedule, Serializable info) throws IOException {

        this.timerId = timerId;
        this.ownerId = ownerId;

        creationTime_ = new Date();
	creationTimeRaw = creationTime_.getTime();

        initialExpirationRaw = initialExpiration.getTime();
        initialExpiration_ = initialExpiration;

        lastExpirationRaw = 0;
        lastExpiration_ = null;

        this.intervalDuration = intervalDuration;
        timerSchedule_ = schedule;
        if (timerSchedule_ != null) {
            this.schedule = timerSchedule_.getScheduleAsString();
        }

        this.containerId = containerId;

        timedObjectPrimaryKey_  = timedObjectPrimaryKey;
        info_ = info;
        blobLoaded_ = true;

        blob = new Blob(timedObjectPrimaryKey, info);
        state = ACTIVE;

    }

    public String stateToString() {
        return stateToString(state);
    }

    public static String stateToString(int state) {
        String stateStr = "UNKNOWN_TIMER_STATE";

        switch(state) {
            case ACTIVE : 
                stateStr = "TIMER_ACTIVE"; 
                break;
            case CANCELLED : 
                stateStr = "TIMER_CANCELLED";
                break;
            default : 
                stateStr = "UNKNOWN_TIMER_STATE";
                break;
        }

        return stateStr;
    }

    private void loadBlob(ClassLoader cl) {
        try {
            timedObjectPrimaryKey_  = blob.getTimedObjectPrimaryKey(cl);
            info_ = blob.getInfo(cl);
            blobLoaded_ = true;
        } catch(Exception e) {
            RuntimeException ex = new RuntimeException();
            ex.initCause(e);
            throw ex;
        }
    }

    @PostLoad
    public void load() {

        lastExpiration_ = (lastExpirationRaw > 0) ? 
            new Date(lastExpirationRaw) : null;
        
        // Populate derived state of immutable cmp fields.
        creationTime_ = new Date(creationTimeRaw);
        initialExpiration_ = new Date(initialExpirationRaw);
        if (schedule != null) {
            timerSchedule_ = new TimerSchedule(schedule);
        }

        // Lazily deserialize Blob state.  This makes the
        // Timer bootstrapping code easier, since some of the Timer
        // state must be loaded from the database before the 
        // container and application classloader are known.
        timedObjectPrimaryKey_ = null;
        info_       = null;
        blobLoaded_ = false;
    }

    public boolean repeats() {
        return (intervalDuration > 0);
    }

    // XXX back pointer into TimerBean - static method call
    public Serializable getInfo() {
        if( !blobLoaded_ ) {
            loadBlob(TimerBean.getContainer(getContainerId()).getClassLoader());
        }
        return info_;
    }

    // XXX back pointer into TimerBean - static method call
    public Object getTimedObjectPrimaryKey() {
        if( !blobLoaded_ ) {
            loadBlob(TimerBean.getContainer(getContainerId()).getClassLoader());
        }
        return timedObjectPrimaryKey_;
    }   

    public Date getCreationTime() {
        return creationTime_;
    }

    public Date getInitialExpiration() {
        return initialExpiration_;
    }

    public Date getLastExpiration() {
        return lastExpiration_;
    }

    public void setLastExpiration(Date lastExpiration) {
        // can be null
        lastExpiration_ = lastExpiration;
        lastExpirationRaw = (lastExpiration != null) ?
            lastExpiration.getTime() : 0;
    }

    public boolean isActive() {
        return (state == ACTIVE);
    }

    public boolean isCancelled() {
        return (state == CANCELLED);
    }

    /**
     * Many DBs have a limitation that at most one field per DB
     * can hold binary data.  As a workaround, store both EJBLocalObject
     * and "info" as a single Serializable blob.  This is necessary 
     * since primary key of EJBLocalObject could be a compound object.
     * This class also isolates the portion of Timer data that is
     * associated with the TimedObject itself.  During deserialization,
     * we must use the application class loader for the timed object,
     * since both the primary key and info object can be application
     * classes.
     *
     */
    public static class Blob implements Serializable {

        private byte[] primaryKeyBytes_ = null;
        private byte[] infoBytes_ = null;

        public Blob() {
        }

        public Blob(Object primaryKey, Serializable info)
            throws IOException {
            if( primaryKey != null ) {
                primaryKeyBytes_ = EJBUtils.serializeObject(primaryKey);
            } 
            if( info != null ) {
                infoBytes_ = EJBUtils.serializeObject(info);
            }
        }
        
        public Object getTimedObjectPrimaryKey(ClassLoader cl) 
            throws Exception {
            Object pKey = null;
            if( primaryKeyBytes_ != null) {
                pKey = EJBUtils.deserializeObject(primaryKeyBytes_, cl);
/**
                if( logger.isLoggable(Level.FINER) ) {
                    logger.log(Level.FINER, "Deserialized blob : " + pKey);
                }
*/
            }
            return pKey;
        }

        public Serializable getInfo(ClassLoader cl) throws Exception {
            Serializable info = null;
            if( infoBytes_ != null) {
                info = (Serializable)EJBUtils.deserializeObject(infoBytes_, cl);
/**
                if( logger.isLoggable(Level.FINER) ) {
                    logger.log(Level.FINER, "Deserialized blob : " + info);
                }
*/
            }
            return info;
        }
    }

}
