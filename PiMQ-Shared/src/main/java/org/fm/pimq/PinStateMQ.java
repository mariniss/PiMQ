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
package org.fm.pimq;

import java.io.Serializable;

/**
 * Define the possible state for Raspberry Pi pins
 *
 * @author Fabio Marini
 */
public enum PinStateMQ implements Serializable {

    /**
     * Low power
     */
    LOW,

    /**
     * High power
     */
    HIGH;
}
