package com.project.populartvseries.common


/*
    The Resource class is a generic wrapper that represents the status of a network call (Success,
    Error, or Loading). It is used to encapsulate the data and its loading state, allowing the UI to
    respond appropriately.
 */


/*
    The out keyword in Kotlin is used to define a type parameter as covariant. Covariance allows you
    to use a more specific type (a subtype) in place of a more general type (a supertype). This is
    useful in situations where you want to ensure that a class or interface can be used in a way
    that is type-safe but still flexible.

    Understanding Covariance (out)
        When you declare a generic type parameter with out, you're telling the compiler that this
        type can only be produced (returned) by the class or interface and not consumed
        (accepted as an argument). In other words, the type can be used in places where a more
        general type is expected.
 */

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}