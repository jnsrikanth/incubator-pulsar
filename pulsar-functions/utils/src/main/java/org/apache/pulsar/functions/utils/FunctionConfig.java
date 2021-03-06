/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.functions.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.SerDe;
import org.apache.pulsar.functions.utils.validation.ConfigValidation;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.NotNull;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isImplementationOfClass;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isImplementationOfClasses;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isListEntryCustom;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isMapEntryCustom;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isPositiveNumber;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isValidFunctionConfig;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isValidResources;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isValidTopicName;
import org.apache.pulsar.functions.utils.validation.ConfigValidationAnnotations.isValidWindowConfig;
import org.apache.pulsar.functions.utils.validation.ValidatorImpls;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@Data
@EqualsAndHashCode
@ToString
@isValidFunctionConfig
public class FunctionConfig {

    public enum ProcessingGuarantees {
        ATLEAST_ONCE,
        ATMOST_ONCE,
        EFFECTIVELY_ONCE
    }

    public enum Runtime {
        JAVA,
        PYTHON
    }


    @NotNull
    private String tenant;
    @NotNull
    private String namespace;
    @NotNull
    private String name;
    @NotNull
    @isImplementationOfClasses(implementsClasses = {Function.class, java.util.function.Function.class})
    private String className;
    @isListEntryCustom(entryValidatorClasses = {ValidatorImpls.TopicNameValidator.class})
    private Collection<String> inputs;
    @isMapEntryCustom(keyValidatorClasses = { ValidatorImpls.TopicNameValidator.class },
            valueValidatorClasses = { ValidatorImpls.SerdeValidator.class }, targetRuntime = ConfigValidation.Runtime.JAVA)
    @isMapEntryCustom(keyValidatorClasses = { ValidatorImpls.TopicNameValidator.class }, targetRuntime = ConfigValidation.Runtime.PYTHON)
    private Map<String, String> customSerdeInputs;
    @isValidTopicName
    private String output;
    @isImplementationOfClass(implementsClass = SerDe.class)
    private String outputSerdeClassName;
    @isValidTopicName
    private String logTopic;
    private ProcessingGuarantees processingGuarantees;
    private Map<String, Object> userConfig;
    private Runtime runtime;
    private boolean autoAck;
    @isPositiveNumber
    private int parallelism;
    @isValidResources
    private Resources resources;
    private String fqfn;
    @isValidWindowConfig
    private WindowConfig windowConfig;
    @isPositiveNumber
    private Long timeoutMs;
}
