# Android Reliable Messaging Challenge

## Introduction
Here at Pushe we have a server which we frequently send messages to from our Android applications. However, the server code is full of bugs and we often lose the message data if it fails to handle a request successfully. And no one here is willing to fix it!!

In this challenge you are going to help us resolve this problem. Don't worry though, you won't have to debug our server code. Instead we want you to develop a reliable-messaging library for Android which will guarantee that our requests are received and processed successfully.

### The Server
Our server is hosted at https://challenge.ronash.co/reliable-messaging. The server accepts HTTP `POST` requests. If it succeeds in handling a request it will respond with a `200` status code and if it fails it will respond with `500`.

## Challenge Requirements
For this challenge you will need to implement a reliable-messaging library and a sample application which utilizes the library. The library and application should be implemented as **separate modules**.

### Library
The library should provide a simple API method which we will call to send messages to a server. The method should accept as parameters the URL which we want to send our message to and the message data we want to send (as a `Map`). It will then send a `POST` request to the URL with the provided message data. The API method should work asynchronously, meaning once it is called it should not wait for the data to be sent successfully and instead should return immediately.

Once the message is given to the library, it should guarantee that it will be sent to and handled by the server. To achieve this, if your initial `POST` request was unsuccessful you should retry until you receive a successful response (a `200` status code). 

The following should also be taken into consideration:
- Retry attempts should be delayed with an [exponential backoff](https://www.google.com/search?q=exponential+backoff) policy.
- The library should be able to handle multiple messages, i.e., it should accept new messages even if the previous ones have not been successfully sent yet.
- The library should keep attempting to send the messages in the background even if the user closes the application before the messages have been successfully sent. 
- [Optional] Provide a way for the application to be informed once a message has successfully been sent by the library.

#### Tests
You should also write tests to show that your library works as intended. Use the testing/mocking framework of your choice.

### Sample Application
Create a sample application to show how to use your library. The application should contain a single Activity which includes a text field and a button and it should use the library to send the contents of the text field to the server every time the button is pressed.

## Expectations
- You may implement the challenge using either Java or Kotlin.
- You may use third-party libraries (e.g., Retrofit, RxJava, ...)  as long as they were not designed specifically for the problem defined in the challenge.
- Clean, readable and maintainable code is an important factor in our evaluation.
- There is no time constraint for the challenge. However, we do not want it to take too much of your time. If you feel like you have spent sufficient time and it is not yet complete you may still send it to us and we will review it. In this case let us know how much time you managed to put for the challenge.

## What to do?
1. Fork this repository and start coding.
2. Commit and push your code to your new repository.
3. Once complete, send a pull request and we will review your code and get back to you.
