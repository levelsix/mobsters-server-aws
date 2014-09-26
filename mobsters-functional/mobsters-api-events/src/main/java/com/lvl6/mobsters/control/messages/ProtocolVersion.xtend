package com.lvl6.mobsters.control.messages

@Data
/**
 * Data structure for encapsulating a three-part semantic version.
 * 
 * A major version mismatch implies hopeless communication compatibility.  Client must upgrade
 * in order to play.  End of session.
 * 
 * A minor version mismatch implies client is out-of-date.  Backwards compatibility is being
 * provided, but game play experience may still be negatively impacted to some degree.  Client 
 * is strongly encouraged to upgrade!
 * 
 * A patch version mismatch implies client is out-of-date.  Backwards compatibility is being 
 * provided, and client is unlikely to notice, but upgrading might improve gameplay through
 * benefits like bug fixes and documentation improvements associated with the change.  Client
 * is casually encouraged to upgrade
 */
class ProtocolVersion {
	int majorVersion;
	int minorVersion;
	int patchVersion;

	// We don't intend to support optional upgrade with maintenance releases, so this is
	// a non-requirement at present.  Keep it in mind should that ever change.  It is the
	// semantic version element for a "hot fix" release applied to a version from the
	// past where you've already moved past its original version tags and so cannot use 
	// them to express compatibility any more, but the fix is urgent enough to break the
	// semantics under a controlled exception case.
	//
	// String maintenanceBranch;
	// int maintenanceVersion;
}