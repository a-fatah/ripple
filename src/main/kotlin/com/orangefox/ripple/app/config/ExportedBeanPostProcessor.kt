package com.orangefox.ripple.app.config

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.core.type.StandardMethodMetadata

class ExportedBeanPostProcessor(private val beanFactory: ConfigurableListableBeanFactory): BeanPostProcessor {

  override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
    return bean
  }

  override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
    if (isExported(beanName)) {
      val parentBeanFactory = getParentBeanFactory()
      parentBeanFactory?.registerSingleton(beanName, bean)
    }

    return bean
  }

  private fun isExported(beanName: String): Boolean {

    try {
      val beanDefinition = beanFactory.getBeanDefinition(beanName)
      val source = beanDefinition.source
      if (source is StandardMethodMetadata) {
        val bean = beanDefinition.source as StandardMethodMetadata
        return bean.isAnnotated(Exported::class.qualifiedName)
      }
    } catch (e: Exception) {
    }
    return false
  }

  fun getParentBeanFactory(): ConfigurableListableBeanFactory? {
    val parentBeanFactory = beanFactory.parentBeanFactory
    if (parentBeanFactory is ConfigurableListableBeanFactory) {
      return parentBeanFactory
    }
    return null
  }
}
