// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.exceptions;

import com.github.shsmysore.fcmjava.http.constants.HttpStatus;

/**
 * This Exception is thrown, if the Authentication with the FCM server failed.
 */
public class FcmAuthenticationException extends FcmException {

    public FcmAuthenticationException(String httpReasonPhrase) {
        super(HttpStatus.UNAUTHORIZED, httpReasonPhrase);
    }

}
