import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThoughtDB {

	private static final Map<String, Set<String>> userThoughtsMap = new HashMap<String, Set<String>>();

	private static ThoughtDB thoughtDB;

	private ThoughtDB() {

	}

	public Set<String> getUserThoughts(String username) {
		return userThoughtsMap.get(username);
	}

	public void addUserThought(String username, String thought) {
		Set<String> userThoughts = userThoughtsMap.get(username);
		if (userThoughts == null) {
			userThoughts = new HashSet<String>();
		}
		userThoughts.add(thought);
		userThoughtsMap.put(username, userThoughts);
	}

	public static ThoughtDB getInstance() {

		if (thoughtDB != null) {
			return thoughtDB;
		}
		synchronized (ThoughtDB.class) {
			if (thoughtDB == null) {
				synchronized (ThoughtDB.class) {
					thoughtDB = new ThoughtDB();
				}
			}

		}
		return thoughtDB;
	}

}
