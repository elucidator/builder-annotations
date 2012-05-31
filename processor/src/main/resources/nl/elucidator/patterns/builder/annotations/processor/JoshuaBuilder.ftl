<#--
  ~ Copyright (C) 2010 Jan-Kees van Andel.
  ~ Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->
<#macro returnmethod element>
    <#switch element.collectionType>
        <#case CollectionType.SET>
        return java.util.Collections.unmodifiableSet(${element.name});
            <#break>
        <#case CollectionType.LIST>
        return java.util.Collections.unmodifiableList(${element.name});
            <#break>
        <#case CollectionType.MAP>
        return java.util.Collections.unmodifiableMap(${element.name});
            <#break>
        <#case CollectionType.SORTED_MAP>
        return java.util.Collections.unmodifiableSortedMap(${element.name});
            <#break>
        <#case CollectionType.SORTED_SET>
        return java.util.Collections.unmodifiableSortedSet(${element.name});
            <#break>
        <#case CollectionType.NONE>
        return ${element.name};
            <#break>
    </#switch>
</#macro>
/*
* Copyright (C) 2010 Jan-Kees van Andel.
* Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package ${targetPackage};

import net.jcip.annotations.Immutable;

@javax.annotation.Generated("${generatorClass}")
/**
* {@inheritDoc}
**/
@Immutable
public final class ${implClassSimpleName} implements ${superClassQName} {

<#list required as property>
/* Property ${property.name} */
private final ${property.type} ${property.name};
</#list>
<#list optional as property>
/* Property ${property.name} */
private final ${property.type} ${property.name};
</#list>

/**
* Classic implementation of the Joshua Bloch
* Builder pattern
**/
public static class Builder {
<#list required as property>
private ${property.type} ${property.name};
</#list>
<#list optional as property>
private ${property.type} ${property.name};
</#list>


public Builder(
<#list required as property>
${property.type} ${property.name}<#if property_has_next>,</#if>
</#list>
) {
<#list required as property>
this.${property.name} = ${property.name};
</#list>
}

/**
* The Builder, build the immutable instance
**/
public ${superClassQName} build() {
return new ${implClassSimpleName}(this);
}

<#list optional as property>
/**
* See {@link  ${superClassQName}#${property.getter}()  ${property.getter}} documentation of the field.
*  @param ${property.name} Value to set
**/
public Builder ${property.name}(${property.type} ${property.name}) {
this.${property.name} = ${property.name};
return this;
}
</#list>

<#list required as property>
/**
* See {@link  ${superClassQName}#${property.getter}()  ${property.getter}} documentation of the field.
*  @param ${property.name} Value to set
**/
public Builder ${property.name}(${property.type} ${property.name}) {
this.${property.name} = ${property.name};
return this;
}
</#list>
}

private ${implClassSimpleName}(Builder builder){
<#list required as property>
this.${property.name} = builder.${property.name};
</#list>
<#list optional as property>
this.${property.name} = builder.${property.name};
</#list>

}


<#list required as property>
/**
* {@inheritDoc}
**/
public ${property.type} ${property.getter}() {
    <@returnmethod property/>
}
</#list>

<#list optional as property>
/**
* {@inheritDoc}
*
**/
public ${property.type} ${property.getter}() {
    <@returnmethod property/>
}

</#list>

/**
* Utility to get a Builder from the immutable object.
**/
public Builder builder() {
Builder b = new Builder(
<#list required as property>
${property.name}<#if property_has_next>,</#if>
</#list>
);


<#list optional as property>
b.${property.name}(${property.name});
</#list>

return b;
}

}
