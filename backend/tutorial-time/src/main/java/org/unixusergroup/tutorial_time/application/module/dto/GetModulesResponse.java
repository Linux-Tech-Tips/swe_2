/**
 * @author Martin
 */
package org.unixusergroup.tutorial_time.application.module.dto;

import java.util.List;

/**
 * GetModules use case Response record
 * @param modules The retrieved list of module codes.
 */
public record GetModulesResponse(List<String> modules) {
}
