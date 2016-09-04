
/**
 *	Autophrases
 *	Author: Christian Madden
 *
 *
 *	Copyright 2016 Christian Madden
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *			http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */

definition(
	name: "HomeAPI Notify",
	namespace: "christianmadden",
	author: "Christian Madden",
	description: "Notifies HomeAPI when a routine has been executed.",
	iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
	iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
	iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
) { appSetting "baseURL" }

def installed()
{
	initialize()
}

def updated()
{
	unsubscribe()
	initialize()
}

def initialize()
{
	subscribe(location, "routineExecuted", onRoutineChange)
}

def onRoutineChange(evt)
{
	def routine = evt.displayName
	log.debug routine
	// Remove non-alphanumeric chars, replace spaces with dashes and lowercase
	routine = routine.replaceAll(/\s/, '-')
	routine = routine.replaceAll(/[^a-zA-Z0-9-]/, '')
	routine = routine.toLowerCase()
	def path = "${appSettings.baseURL}/api/routine/${routine}"

	try
	{
		httpGet(path) { resp -> }
	}
	catch(e)
	{
		log.debug "Error: $e"
	}
}
