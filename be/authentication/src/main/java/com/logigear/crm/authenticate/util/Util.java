package com.logigear.crm.authenticate.util;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import java.io.IOException;

public class Util {

	private static ObjectMapper objectMapper;
	
	public Util(ObjectMapper objectMapper) {
		Util.objectMapper = objectMapper;
	}

	@SuppressWarnings("unchecked")
	public static<T> T applyPatch(T originalObj, String patchString) {

		try {
			 // Parse the patch to JsonNode
	        JsonNode patchNode = objectMapper.readTree(patchString);

	        // Create the patch
	        JsonPatch patch = JsonPatch.fromJson(patchNode);

	        // Convert the original object to JsonNode
	        JsonNode originalObjNode = objectMapper.valueToTree(originalObj);

	        // Apply the patch
	        TreeNode patchedObjNode = patch.apply(originalObjNode);

	        // Convert the patched node to an updated obj
	        return objectMapper.treeToValue(patchedObjNode, (Class<T>) originalObj.getClass());
		} catch (IOException | JsonPatchException e) {
			throw new RuntimeException(e);
		}
	}
}
