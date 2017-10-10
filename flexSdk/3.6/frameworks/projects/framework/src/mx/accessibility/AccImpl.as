////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2003-2006 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package mx.accessibility
{

import flash.accessibility.Accessibility;
import flash.accessibility.AccessibilityImplementation;
import flash.accessibility.AccessibilityProperties;
import flash.display.DisplayObject;
import flash.display.DisplayObjectContainer;
import flash.events.Event;
import flash.system.ApplicationDomain;

import mx.core.IFlexModuleFactory;
import mx.core.UIComponent;
import mx.core.mx_internal;
import mx.managers.ISystemManager;
import mx.resources.ResourceManager;
import mx.resources.IResourceManager;

use namespace mx_internal;

[ResourceBundle("controls")]

/**
 *  The AccImpl class is Flex's base class for implementing accessibility
 *  in UIComponents.
 *  It is a subclass of the Flash Player's AccessibilityImplementation class.
 */ 
public class AccImpl extends AccessibilityImplementation
{
    include "../core/Version.as";

    //--------------------------------------------------------------------------
    //
    //  Class constants
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     */
    private static const STATE_SYSTEM_NORMAL:uint = 0x00000000;

    /**
     *  @private
     */
    private static const STATE_SYSTEM_FOCUSABLE:uint = 0x00100000;
    
    /**
     *  @private
     */
    private static const STATE_SYSTEM_FOCUSED:uint = 0x00000004;
    
    /**
     *  @private
     */
    private static const STATE_SYSTEM_UNAVAILABLE:uint = 0x00000001;
    
    /**
     *  @private
     */
    private static const EVENT_OBJECT_NAMECHANGE:uint = 0x800C;

    /**
     *  @private
     */
    public static const EVENT_OBJECT_SHOW:uint = 0x8002;
    
    /**
     *  @private
     */
    private static const EVENT_OBJECT_HIDE:uint = 0x8003;
        
    //--------------------------------------------------------------------------
    //
    //  Class methods
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  
     *  Get the definition of a class, namespace, or function. The defintion is
     *  obtained from the specified moduleFactory. If there is no moduleFactory,
     *  then the definition of looked up in ApplicationDomain.currentDomain.
     * 
     *  @param name name of the class, namespace, or function to get.
     *  @param moduleFactory The moduleFactory that specifies the application 
     *  domain to use to find the name. If moduleFactory is null, then
     *  ApplicationDomain.currentDomain is used as a fall back.
     * 
     *  return a class, namespace, or function. 
     * 
     */ 
    mx_internal static function getDefinition(name:String, moduleFactory:IFlexModuleFactory):Object
    {
        var currentDomain:ApplicationDomain;
        
        // Use the given module factory to look for the domain. If the module 
        // factory is null then fall back to Application.currentDomain.
        if (moduleFactory)
            currentDomain = moduleFactory.info()["currentDomain"];
        else
            currentDomain = ApplicationDomain.currentDomain;

        if (currentDomain.hasDefinition(name))
            return currentDomain.getDefinition(name);
        
        return null;
    }
    
    /**
     *  Method for supporting state Accessibility.
     *  Returns true if an ancestor of the component has enabled set to false.
     */
    public static function isAncestorDisabled(component:UIComponent):Boolean
    {
        // keeping this DisplayObjectContainer since parent returns
        // that as root is not a UIComponent.
        var par:DisplayObjectContainer = component.parent;

        // continue looking up the parent chain
        // until a disabled UIComponent is found
		// stopping at the root or system manager
        while (par && (par is UIComponent && UIComponent(par).enabled) &&
               !(par is ISystemManager) && par != component.root)
        {
            par = par.parent;
        }

        if (!(par is UIComponent))
            return false;
            
        return !UIComponent(par).enabled;
    }
    
    /**
     *  Method for supporting Form Accessibility.
     */
    public static function getFormName(component:UIComponent):String
    {
        var formName:String = "";
        
        // Return nothing if we are a container 
        var containerClass:Class = Class(getDefinition("mx.core.Container", component.moduleFactory));
        if (containerClass && component is containerClass)
            return formName;

        // keeping this DisplayObjectContainer since parent returns
        // that as root is not a UIComponent.
        var formItemClass:Class = Class(getDefinition("mx.containers.FormItem", component.moduleFactory));
        var par:DisplayObjectContainer = component.parent; 

        // continue looking up the parent chain
		// until a FormItem is found
        // stopping at the root or system manager
        while (par && !(formItemClass && par is formItemClass) &&
               !(par is ISystemManager) && par != component.root)
        {
            par = par.parent;
        }

        if (par && formItemClass && par is formItemClass)
            formName = updateFormItemString(par);

        return formName;
    }
    
    /**
     *  @private
     *  Method for supporting Form Accessibility.
     */
    private static function joinWithSpace(s1:String,s2:String):String
    {
        // Single space treated as null so developers can override default name elements with " ".
        if (s1 == " ")
            s1 = "";
        if (s2 == " ")
            s2 = "";
        if (s1 && s2)
            s1 += " " +s2;
        else if (s2)
            s1 = s2;
        // else we have non-empty s1 and empty s2, so do nothing.
        return s1;
    }

    /**
     *  @private
     *  Method for supporting Form Accessibility.
     * 
     *  @param formItem Object of type FormItem. Object is used here to avoid
     *  linking in FormItem. 
     */
    private static function updateFormItemString(formItem:Object):String
    {
        var formName:String = "";
        var resourceManager:IResourceManager = ResourceManager.getInstance();
        
        var formClass:Class = Class(getDefinition("mx.containers.Form", formItem.moduleFactory));
        var form:UIComponent = UIComponent(formItem.parent);

        // If we are located within a Form, then look for the first FormHeading
        // that is a sibling that is above us in the parent's child hierarchy
        if (formClass && form is formClass)
        {
            var formHeadingClass:Class = Class(getDefinition("mx.containers.FormHeading", formItem.moduleFactory));
            var formItemIndex:int = form.getChildIndex(DisplayObject(formItem));
            for (var i:int = formItemIndex; i >= 0; i--)
            {
                var child:UIComponent = UIComponent(form.getChildAt(i));
                if (formHeadingClass && child is formHeadingClass)
                {
                    // Accessible name if it exists, else label text.
                    if (formHeadingClass(child).accessibilityProperties)
                        formName = 
                            formHeadingClass(child).accessibilityProperties.name;
                    if (formName == "") 
                        formName = formHeadingClass(child).label;
                    break;
                }
            }
        }

        // Add in "Required Field" text if we are a required field
        if (formItem.required)
            formName = joinWithSpace(formName,
                resourceManager.getString("controls","requiredField"))

        // Add in the label from the formItem
        // Accessible name if it exists, else label text.
        var f:String = "";
        if (formItem.accessibilityProperties)
        {
            f = formItem.accessibilityProperties.name
            if (formItem.itemLabel && formItem.itemLabel.accessibilityEnabled)
                formItem.itemLabel.accessibilityEnabled = false;
        }
        if (f == "")
            f = formItem.label;
            
        formName = joinWithSpace(formName, f);

        return formName;
    }

    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     *
     *  @param master The UIComponent instance that this AccImpl instance
     *  is making accessible.
     */
    public function AccImpl(master:UIComponent)
    {
        super();

        this.master = master;
        
        stub = false;
        
        // Hook in UIComponentAccProps setup here!
        if (!master.accessibilityProperties)
            master.accessibilityProperties = new AccessibilityProperties();
        
        // Hookup events to listen for
        var events:Array = eventsToHandle;
        if (events)
        {
            var n:int = events.length;
            for (var i:int = 0; i < n; i++)
            {
                master.addEventListener(events[i], eventHandler);
            }
        }
    }

    //--------------------------------------------------------------------------
    //
    //  Variables
    //
    //--------------------------------------------------------------------------

    /**
     *  A reference to the UIComponent instance that this AccImpl instance
     *  is making accessible.
     */
    protected var master:UIComponent;
    
    /**
     *  Accessibility role of the component being made accessible.
     */
    protected var role:uint;
    
    //--------------------------------------------------------------------------
    //
    //  Properties
    //
    //--------------------------------------------------------------------------

    /**
     *  All subclasses must override this function by returning an array
     *  of strings of the events to listen for.
     */
    protected function get eventsToHandle():Array
    {
        return [ "errorStringChanged", "toolTipChanged", "show", "hide" ];
    }
    
    //--------------------------------------------------------------------------
    //
    //  Overridden methods: AccessibilityImplementation
    //
    //--------------------------------------------------------------------------

    /**
     *  @private
     *  Returns the system role for the component.
     *
     *  @param childID uint.
     *
     *  @return Role associated with the component.
     *
     *  @tiptext Returns the system role for the component
     *  @helpid 3000
     */
    override public function get_accRole(childID:uint):uint
    {
        return role;
    }
    
    /**
     *  @private
     *  Returns the name of the component.
     *
     *  @param childID uint.
     *
     *  @return Name of the component.
     *
     *  @tiptext Returns the name of the component
     *  @helpid 3000
     */
    override public function get_accName(childID:uint):String
    {
        var accName:String;

        // For simple children, do not include anything but the default name.
        // Examples: combo box items, list items, etc.
        if (childID)
        {
            accName = getName(childID);
            // Historical: Return null and not "" for empty and null values.
            return (accName != null && accName != "") ? accName : null;
        }

        // Start with form header and/or formItem label text.
        // Also includes "Required Field" where appropriate.
        accName = getFormName(master);

        // Now the component's name or toolTip.
        if (master.accessibilityProperties && 
            master.accessibilityProperties.name != null && 
            master.accessibilityProperties.name != "")
        {
            // An accName is set, so use only that.
            accName = joinWithSpace(accName, master.accessibilityProperties.name);
        }
        else
        {
            // No accName set; use default name, or toolTip if that is empty.
            accName = joinWithSpace(accName, getName(0) || master.toolTip);
        }
        
        accName = joinWithSpace(accName, getStatusName());
        
        // Historical: Return null and not "" for empty and null values.
        return (accName != null && accName != "") ? accName : null;
    }

    /**
     *  @private
     *  Method to return an array of childIDs.
     *
     *  @return Array
     */
    override public function getChildIDArray():Array
    {
        return [];
    }
    
    /**
     *  @private
     *  IAccessible method for giving focus to a child item in the component
     *  (but not to the component itself; accSelect() is never called
     *  with a childID of 0).
     *  Even though this method does nothing, without it the Player
     *  causes an IAccessible "Member not found" error.
     */
    override public function accSelect(selFlag:uint, childID:uint):void
    {
    }

    //--------------------------------------------------------------------------
    //
    //  Methods
    //
    //--------------------------------------------------------------------------

    /**
     *  Returns the name of the accessible component.
     *  All subclasses must implement this
     *  instead of implementing get_accName().
     */
    protected function getName(childID:uint):String
    {
        return null;
    }
    
    /**
     *  Utility method to determine state of the accessible component.
     */
    protected function getState(childID:uint):uint
    {
        var accState:uint = STATE_SYSTEM_NORMAL;
        
        if (!UIComponent(master).enabled || isAncestorDisabled(master))
        {
            accState &= ~STATE_SYSTEM_FOCUSABLE;
            accState |= STATE_SYSTEM_UNAVAILABLE;
        }
        else
        {
            accState |= STATE_SYSTEM_FOCUSABLE
        
            if (UIComponent(master) == UIComponent(master).getFocus())
                accState |= STATE_SYSTEM_FOCUSED;
        }

        return accState;
    }

    /**
     *  @private
     */
    private function getStatusName():String
    {
        var statusName:String = "";

        if (master is UIComponent && UIComponent(master).errorString)
            statusName = UIComponent(master).errorString;

        return statusName;
    }
    
    /**
     *  @private
     */
    protected function createChildIDArray(n:int):Array
    {
        var a:Array = new Array(n);
        
        for (var i:int = 0; i < n; i++)
        {
            a[i] = i + 1;
        }
        
        return a;
    }
    
    //--------------------------------------------------------------------------
    //
    //  Event handlers
    //
    //--------------------------------------------------------------------------

    /**
     *  Generic event handler.
     *  All AccImpl subclasses must implement this
     *  to listen for events from its master component. 
     */
    protected function eventHandler(event:Event):void
    {
        $eventHandler(event);
    }

    /**
     *  @private
     *  Handles events common to all accessible UIComponents.
     */
    protected final function $eventHandler(event:Event):void
    {
        switch (event.type)
        {
            case "errorStringChanged":
            case "toolTipChanged":
            {
                Accessibility.sendEvent(master, 0, EVENT_OBJECT_NAMECHANGE);
                Accessibility.updateProperties();
                break;
            }

            case "show":
            {
                Accessibility.sendEvent(master, 0, EVENT_OBJECT_SHOW);
                Accessibility.updateProperties();
                break;
            }

            case "hide":
            {
                Accessibility.sendEvent(master, 0, EVENT_OBJECT_HIDE);
                Accessibility.updateProperties();
                break;
            }            
        }
    }
}

}
