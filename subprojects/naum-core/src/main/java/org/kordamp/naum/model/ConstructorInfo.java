/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.naum.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Andres Almiray
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ConstructorInfo extends MemberInfo<ConstructorInfo> {
    public static final String NAME = "<init>";

    private final String argumentTypes;
    private final String[] exceptions;

    private ConstructorInfo(int modifiers, String argumentTypes, String[] exceptions) {
        super(NAME, modifiers);
        this.argumentTypes = argumentTypes;
        this.exceptions = exceptions;
    }

    @Builder(builderMethodName = "constructorInfo")
    public static ConstructorInfo create(int modifiers, String argumentTypes, @Nonnull String[] exceptions) {
        String[] values = exceptions != null ? exceptions : EMPTY;
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].replace('/', '.');
        }
        Arrays.sort(values);
        argumentTypes = argumentTypes == null ? "" : argumentTypes;
        return new ConstructorInfo(modifiers, argumentTypes, values);
    }

    @Override
    public String getContent() {
        StringBuilder b = new StringBuilder("CT{")
            .append("D=")
            .append(getModifiers());

        if (!getAnnotations().isEmpty()) {
            b.append("#A=[");
            for (int i = 0; i < getAnnotations().size(); i++) {
                if (i != 0) { b.append(","); }
                b.append(getAnnotations().get(i).getContent());
            }
            b.append("]");
        }

        if (argumentTypes != null && argumentTypes.length() > 0) {
            b.append("#R=")
                .append(argumentTypes);
        }

        if (exceptions.length > 0) {
            b.append("#E=[");
            for (int i = 0; i < exceptions.length; i++) {
                if (i != 0) { b.append(","); }
                b.append(exceptions[i]);
            }
            b.append("]");
        } b.append("}");

        return b.toString();
    }
}
