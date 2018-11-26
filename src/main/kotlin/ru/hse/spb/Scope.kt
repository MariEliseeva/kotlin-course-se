package ru.hse.spb

class Scope<T>(var parent: Scope<T>? = null) {
    private val items = HashMap<String, T?>()

    fun getItem(name: String): T {
        return if (name in items) {
            items[name] ?: throw ScopeException("Value for item $name is not set")
        } else {
            parent?.getItem(name) ?: throw ScopeException("Item $name is not in current scope")
        }
    }

    fun setItem(name: String, value: T) {
        if (name in items) {
            items[name] = value
        } else {
            parent?.setItem(name, value) ?: throw ScopeException("Item $name is not in current scope")
        }
    }

    fun addItem(name: String, value: T? = null) {
        if (name in items) {
            throw ScopeException("An attempt to add item $name twice")
        } else {
            items[name] = value
        }
    }
}