// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.github.shsmysore.fcmjava.exceptions;

import com.github.shsmysore.fcmjava.http.constants.HttpStatus;

/**
 * This Exception is thrown, if a Bad Request to FCM was made.
 */
public class FcmBadRequestException extends FcmException {

    public FcmBadRequestException(String httpReasonPhrase) {
        super(HttpStatus.BAD_REQUEST, httpReasonPhrase);
    }



}
