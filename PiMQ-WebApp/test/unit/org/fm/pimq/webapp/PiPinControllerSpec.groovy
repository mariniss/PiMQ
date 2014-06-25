/**
 * Copyright 2014 Fabio Marini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fm.pimq.webapp



import grails.test.mixin.*
import spock.lang.*

@TestFor(PiPinController)
@Mock(PiPin)
class PiPinControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.piPinInstanceList
            model.piPinInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.piPinInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            def piPin = new PiPin()
            piPin.validate()
            controller.save(piPin)

        then:"The create view is rendered again with the correct model"
            model.piPinInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            piPin = new PiPin(params)

            controller.save(piPin)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/piPin/show/1'
            controller.flash.message != null
            PiPin.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def piPin = new PiPin(params)
            controller.show(piPin)

        then:"A model is populated containing the domain instance"
            model.piPinInstance == piPin
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def piPin = new PiPin(params)
            controller.edit(piPin)

        then:"A model is populated containing the domain instance"
            model.piPinInstance == piPin
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/piPin/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def piPin = new PiPin()
            piPin.validate()
            controller.update(piPin)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.piPinInstance == piPin

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            piPin = new PiPin(params).save(flush: true)
            controller.update(piPin)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/piPin/show/$piPin.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/piPin/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def piPin = new PiPin(params).save(flush: true)

        then:"It exists"
            PiPin.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(piPin)

        then:"The instance is deleted"
            PiPin.count() == 0
            response.redirectedUrl == '/piPin/index'
            flash.message != null
    }
}
