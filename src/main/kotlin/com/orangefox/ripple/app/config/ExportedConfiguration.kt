package com.orangefox.ripple.app.config

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ExportedConfiguration(var beanFactory: ConfigurableListableBeanFactory): BeanFactoryAware {

  override fun setBeanFactory(beanFactory: BeanFactory) {
    this.beanFactory = beanFactory as ConfigurableListableBeanFactory
  }

  @Bean
  open fun exportedBeanPostProcessor(): BeanPostProcessor {
    return ExportedBeanPostProcessor(beanFactory)
  }

}
