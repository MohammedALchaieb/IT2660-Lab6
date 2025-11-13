class Main {
  public static void main(String[] args) {
    // Step 3: Create a map called creditHours
    MyMap<String, Integer> creditHours = new MyHashMap<>();

    creditHours.put("IT-1025", 3);
    creditHours.put("IT-1050", 3);
    creditHours.put("IT-1150", 3);
    creditHours.put("IT-2310", 3);
    creditHours.put("IT-2320", 4);
    creditHours.put("IT-2351", 4);
    creditHours.put("IT-2650", 4);
    creditHours.put("IT-2660", 4);
    creditHours.put("IT-2030", 4);

    System.out.println("Has IT-1025? " + creditHours.containsKey("IT-1025"));
    System.out.println("Has IT-2110? " + creditHours.containsKey("IT-2110"));

    System.out.println("All key/value pairs in map:");
    for (MyMap.Entry<String, Integer> entry : creditHours.entrySet()) {
      System.out.println(entry.getKey() + " -> " + entry.getValue());
    }

    creditHours.remove("IT-2030");
    creditHours.remove("IT-1150");

    System.out.println("All values after removals:");
    for (Integer value : creditHours.values()) {
      System.out.println(value);
    }
  }
}
