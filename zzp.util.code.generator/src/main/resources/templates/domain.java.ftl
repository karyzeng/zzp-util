package ${package.Entity}.domain;

import ${package.Entity}.${entity};
import java.io.Serializable;
import org.springframework.stereotype.Component;
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * <p>
 * ${table.comment!} 领域类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
<#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
<#else>
@EqualsAndHashCode(callSuper = false)
</#if>
@Accessors(chain = true)
</#if>
@Component
public class ${entity}Domain extends ${entity} implements Serializable {

<#if entitySerialVersionUID>
     private static final long serialVersionUID = 1L;
</#if>

}
