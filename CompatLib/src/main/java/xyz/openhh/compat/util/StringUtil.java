/*
 * Copyright 2015 HH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.openhh.compat.util;


/**
 * Created by HH .
 */
public final class StringUtil {
    /**
     * @param str
     *
     * @return
     */
    public final static boolean isNull(String str) {
        return null == str || 0 == str.length();
    }
}
