package de.fyreum.customitemsxl;

public class TestClass {

    public static void main(String[] args) {
        System.out.println(newElo(2000));
        // System.out.println(toString(new String[]{"Test", "String", "array"}));
        /*
        StringBuilder b = new StringBuilder();


        Multimap<String, String> multi = HashMultimap.create();
        Map<String, Collection<String>> map = multi.asMap();

        multi.put("key1", "value1");
        multi.put("key1", "value2");
        multi.put("key2", "value3");

        int max = multi.size();
        System.out.println("max: " + max);

        int i = 0;
        for (String a : map.keySet()) {
            for (String s : map.get(a)) {
                b.append(a).append(":").append(s);
                if (++i != max) {
                    b.append(",");
                } else {
                    b.append(";");
                }
            }
        }
        System.out.println(b.toString());
        */
        // System.out.println(Util.toShapeString(Util.toShape("000010000")));
        // System.out.println(Arrays.toString(Util.addToArray(new Integer[]{1, 2, 3, 4}, 5, 6, 7)));
    }

    public static String toString(String[] a) {
        int iMax = a.length - 1;

        StringBuilder b = new StringBuilder();
        b.append('{');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append('}').toString();
            b.append(",");
        }
    }

    public static int newElo(int oldElo) {
        return oldElo < 1400 ? oldElo : (1400 + (oldElo - 1400) / (3 - (3000 - oldElo) / 800));
    }

}
