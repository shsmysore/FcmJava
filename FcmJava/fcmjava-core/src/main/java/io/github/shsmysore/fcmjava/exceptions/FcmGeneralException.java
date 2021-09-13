// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.exceptions;

public class FcmGeneralException extends FcmException {

    public FcmGeneralException(int httpStatusCode, String reasonPhrase) {
        super(httpStatusCode, reasonPhrase);
    }

}
