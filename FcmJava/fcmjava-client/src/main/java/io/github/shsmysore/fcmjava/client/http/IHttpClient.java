// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.github.shsmysore.fcmjava.client.http;

import java.util.concurrent.CompletableFuture;

/**
 * An DefaultHttpClient is used to send Requests to FCM.
 */
public interface IHttpClient {

    <TRequestMessage, TResponseMessage> TResponseMessage postSync(TRequestMessage requestMessage, Class<TResponseMessage> responseType);

    <TRequestMessage, TResponseMessage> CompletableFuture<TResponseMessage> postAsync(TRequestMessage requestMessage, Class<TResponseMessage> responseType);

}
