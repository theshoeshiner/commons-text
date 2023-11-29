/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.cases;

/**
 * Case implementation that parses and formats strings of the form 'myCamelCase'
 * <p>
 * CamelCase is a case where tokens are delimited by upper case Unicode characters. The very first
 * token should begin with a lower case character, and any subsequent tokens begin with an
 * upper case character. All remaining characters will be lower case or non cased.
 * </p>
 */
public final class CamelCase extends UpperCaseDelimitedCase {

    /** Constant reusable instance of this case. */
    public static final CamelCase INSTANCE = new CamelCase();

    /**
     * Constructs new CamelCase instance.
     */
      private CamelCase() {
          super(true);
      }

}
