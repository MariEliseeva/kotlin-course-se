package ru.hse.spb

class InterpretationException (lineNumber : Int, message : String) : Exception("Error in line $lineNumber: $message")

class ScopeException (message : String) : Exception(message)