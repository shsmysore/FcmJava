// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.fcmjava.client.http;

import java.util.concurrent.CompletableFuture;

/**
 * An DefaultHttpClient is used to send Requests to FCM.
 */
public interface IHttpClient extends AutoCloseable {

    <TRequestMessage> void post(TRequestMessage requestMessage);

    <TRequestMessage, TResponseMessage> TResponseMessage post(TRequestMessage requestMessage, Class<TResponseMessage> responseType);

    <TRequestMessage> CompletableFuture<String> postAsync(TRequestMessage requestMessage);

}
