/*
 * Copyright 2012-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.repository.config;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.generator.AotContributingBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.aot.AotContributingRepositoryBeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * SPI to implement store specific extension to the repository bean definition registration process.
 *
 * @see RepositoryConfigurationExtensionSupport
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public interface RepositoryConfigurationExtension {

	/**
	 * A {@link String} uniquely identifying the module within all Spring Data modules. Must not contain any spaces.
	 *
	 * @return will never be {@literal null}.
	 * @since 3.0
	 */
	default String getModuleIdentifier() {

		return getModuleName()
				.toLowerCase(Locale.ENGLISH)
				.replace(' ', '-');
	}

	/**
	 * Returns the descriptive name of the module.
	 *
	 * @return will never be {@literal null}.
	 */
	default String getModuleName() {
		return StringUtils.capitalize(getModulePrefix());
	}

	String getModulePrefix();

	/**
	 * Returns all {@link RepositoryConfiguration}s obtained through the given {@link RepositoryConfigurationSource}.
	 *
	 * @param configSource
	 * @param loader
	 * @param strictMatchesOnly whether to return strict repository matches only. Handing in {@literal true} will cause
	 *          the repository interfaces and domain types handled to be checked whether they are managed by the current
	 *          store.
	 * @return will never be {@literal null}.
	 * @since 1.9
	 */
	<T extends RepositoryConfigurationSource> Collection<RepositoryConfiguration<T>> getRepositoryConfigurations(
			T configSource, ResourceLoader loader, boolean strictMatchesOnly);

	/**
	 * Returns the default location of the Spring Data named queries.
	 *
	 * @return will never be {@literal null}.
	 */
	String getDefaultNamedQueryLocation();

	/**
	 * Returns the name of the repository factory class to be used.
	 *
	 * @return will never be {@literal null}.
	 */
	String getRepositoryFactoryBeanClassName();

	/**
	 * @return the {@link AotContributingBeanPostProcessor} type responsible for contributing AOT/native configuration.
	 *         Defaults to {@link AotContributingRepositoryBeanPostProcessor}. Must not be {@literal null}
	 * @since 3.0
	 */
	default Class<? extends AotContributingBeanPostProcessor> getAotPostProcessor() {
		return AotContributingRepositoryBeanPostProcessor.class;
	}

	/**
	 * Callback to register additional bean definitions for a {@literal repositories} root node. This usually includes
	 * beans you have to set up once independently of the number of repositories to be created. Will be called before any
	 * repositories bean definitions have been registered.
	 *
	 * @param registry
	 * @param configurationSource
	 */
	void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource);

	/**
	 * Callback to post process the {@link BeanDefinition} and tweak the configuration if necessary.
	 *
	 * @param builder will never be {@literal null}.
	 * @param config will never be {@literal null}.
	 */
	void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource config);

	/**
	 * Callback to post process the {@link BeanDefinition} built from annotations and tweak the configuration if
	 * necessary.
	 *
	 * @param builder will never be {@literal null}.
	 * @param config will never be {@literal null}.
	 */
	void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config);

	/**
	 * Callback to post process the {@link BeanDefinition} built from XML and tweak the configuration if necessary.
	 *
	 * @param builder will never be {@literal null}.
	 * @param config will never be {@literal null}.
	 */
	void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config);
}
