package com.orangefox.ripple.commons

import com.orangefox.ripple.app.events.publisher.DomainEvent
import com.orangefox.ripple.commons.entities.User

class UserCreated(val user: User): DomainEvent(user.id!!)
