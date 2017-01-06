/**
 *  Physical Tap
 *  Based on Double Duty by Pasquale Ranalli
 *
 *  Copyright 2015 Pasquale Ranalli
 *  Copyright 2017 Matt Stocum
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Physical Tap",
    namespace: "mattstocum",
    author: "Matt Stocum",
    description: "This app allows you to use physical switch presses to control additional lights.",  
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
)

preferences {
    section("The Master switch whose on and/or off buttons will serve as controls") {
    paragraph "NOTE: Plain on/off switches are preferable to dimmers.  Be mindful that dimmers may trigger unexpected toggles when turned off or dimmed to 0 (zero).  You've been warned!"
        input "master", "capability.switch", title: "Select", required: true
    }

    section("On presses will switch") {
        input "onSlaves", "capability.switch", multiple: true, required: false, title: "Select"
    }

	section("Off presses will switch") {
        input "offSlaves", "capability.switch", multiple: true, required: false, title: "Select"
    }
}

def installed(){
	log.debug "Physical Tap installed"
    subscribe(master, "switch", switchHandler)
}

def updated(){
	log.debug "Physical Tap updated"
    unsubscribe()
    subscribe(master, "switch", switchHandler)
}

def switchHandler(evt) {
	log.debug evt.name
	log.debug "Event name: ${evt.name}, physical: ${evt.physical}, value: ${evt.value}"
    if (evt.name == "switch" && evt.physical) {
    	log.debug "Master Switch physical switch event"
    	switch (evt.value) {
        	case "on": 
            	log.debug "Settings slaves on"
        		onSlaves*.on()
            	break
	        case "off":
            	log.debug "Setting slaves off"
	        	offSlaves*.off()
	            break
		}
    }	
}