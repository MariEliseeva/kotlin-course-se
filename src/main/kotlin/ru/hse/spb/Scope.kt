package ru.hse.spb

class Scope<T>(var parent: Scope<T>? = null) {
    private val items = HashMap<String, T?>()

    fun getItem(name: String): T {
        if (items.containsKey(name)) {
            val value = items[name]
            if (value != null) {
                return value
            }
            throw ScopeException("Value for item $name is not set")
        } else {
            if (parent != null) {
                return parent!!.getItem(name)
            }
            throw ScopeException("Item $name is not in current scope")
        }
    }

    fun setItem(name: String, value: T) {
        if (items.containsKey(name)) {
            items[name] = value
        } else {
            if (parent != null) {
                parent!!.setItem(name, value)
            } else {
                throw ScopeException("Item $name is not in current scope")
            }
        }
    }

    fun addItem(name: String, value: T? = null) {
        if (items.containsKey(name)) {
            throw ScopeException("An attempt to add item $name twice")
        } else {
            items[name] = value
        }
    }
}