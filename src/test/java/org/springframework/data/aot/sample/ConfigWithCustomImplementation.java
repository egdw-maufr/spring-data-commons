/*
 * Copyright 2019-2021 the original author or authors.
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
package org.springframework.data.aot.sample;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.config.EnableRepositories;
import org.springframework.stereotype.Component;

/**
 * @author Christoph Strobl
 */
@Configuration
@EnableRepositories(considerNestedRepositories = true,
		includeFilters = { @Filter(type = FilterType.REGEX, pattern = ".*RepositoryWithCustomImplementation") })
public class ConfigWithCustomImplementation {

	public interface RepositoryWithCustomImplementation extends Repository<Person, String>, CustomImplInterface {

	}

	public interface CustomImplInterface {

		List<Person> findMyCustomer();
	}

	@Component
	public static class RepositoryWithCustomImplementationImpl implements CustomImplInterface {

		@Override
		public List<Person> findMyCustomer() {
			return Collections.emptyList();
		}
	}

	public static class Person {

		Address address;

	}

	public static class Address {
		String street;
	}
}
