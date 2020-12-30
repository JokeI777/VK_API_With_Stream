import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.*;

//Return top names with their count met in members VK group
//Example group id: iritrtf_urfu
public class VK_API {
    public static void main(String[] args) throws IOException, JSONException {
        System.out.print("Enter groupID: ");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String groupID = buffer.readLine();
        Map<String, Integer> members = getPopularNamesFromGroup(groupID);

        members.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(System.out::println);
    }

    public static Map<String, Integer> getPopularNamesFromGroup(String groupID) throws IOException {
        //Enter ur token VK!
        Scanner scanner = new Scanner(Paths.get("C:\\go\\Token_Vk_api.txt"));
        String token = scanner.nextLine();
        String api = "https://api.vk.com/method/groups.getMembers?" +
                "group_id=" + groupID +
                "&fields=city&access_token=" +
                token;
        String membersInfo = getMembersInfo(api);
        Map<String, Integer> members = getDitctionaryPopular(membersInfo);
        return members;
    }

    public static String getMembersInfo(String api) throws IOException {
        URL url = new URL(api);
        URLConnection c = url.openConnection();
        Scanner scanner = new Scanner(new BufferedReader(
                new InputStreamReader(
                        c.getInputStream())));

        StringBuilder s = new StringBuilder();
        while (scanner.hasNextLine()) {
            s.append(scanner.nextLine());
        }
        scanner.close();
        return s.toString();
    }

    public static Map<String, Integer> getDitctionaryPopular(String members){
        Map<String, Integer> dictionary = new HashMap<>();
        JSONObject json = new JSONObject(members);
        JSONArray arrayMembers = json.getJSONObject("response").getJSONArray("items");
        for(int i = 0; i < arrayMembers.length(); i++)
        {
            String name = arrayMembers.getJSONObject(i).get("first_name").toString();
            if(dictionary.containsKey(name)){
                dictionary.put(name, dictionary.get(name) + 1);
            }
            else{
                dictionary.put(name, 1);
            }
        }
        return dictionary;
    }
}
