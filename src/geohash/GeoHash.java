package geohash;



import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


// from https://github.com/kungfoo/geohash-java/
@SuppressWarnings({"JavaDoc"})
public final class GeoHash implements Comparable<GeoHash>, Serializable {


    public static void main(String[] args) {

        //Varible for reading the csv file
        String current = System.getProperty("user.dir");
        String csvFile = current+"/src/data/in/newyork.csv";
        String line = "";
        String cvsSplitBy = ",";

        //variable to record the content of our csv file

        Integer cmplnt_num;
        Date cmplnt_fr_dt;;
        Date cmplnt_fr_tm;
        Date cmplnt_to_dt;
        Date cmplnt_to_tm;
        Date rpt_dt;
        Integer ky_cd;
        String ofns_desc;
        Integer pd_cd;
        String pd_desc;
        String crm_atpt_cptd_cd;
        String law_cat_cd;
        String juris_desc;
        String boro_nm;
        Integer addr_pct_cd;
        String loc_of_occur_desc;
        String prem_typ_desc;
        String parks_nm;
        String hadevelopt;
        Integer x_coord_cd;
        Integer y_coord_cd;
        Double latitude;
        Double longitude;
        String lat_lon;

        //Variables to detect outliers

        //variable of Geo Hash
        GeoHash new_hash;
       
        //varibles for Hash Time
        String day_night;
        Integer month;
        int dayOfWeek;
        int weekOfYear;
        int weekOfMonth;
        int dayOFMonth;
        int hour;



        //variable for conserving new line content
        String newline;




        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            //Reading the first colones names string line
            BufferedWriter bw = new BufferedWriter(new FileWriter(current+"/src/data/out/updatenewyork.csv"));




            newline="CMPLNT_NUM,CMPLNT_FR_DT,CMPLNT_FR_TM,CMPLNT_TO_DT,CMPLNT_TO_TM,RPT_DT=,KY_CD"
                    + ",OFNS_DESC,PD_CD,PD_DESC,CRM_ATPT_CPTD_CD,LAW_CAT_CD,JURIS_DESC,BORO_NM,ADDR_PCT_CD" +
                    ",LOC_OF_OCCUR_DESC,PREM_TYP_DESC,PARKS_NM,HADEVELOPT,X_COORD_CD,Y_COORD_CD,Latitude,Longitude,Lat_Lon"+
                    ",GeoHash,Week of year,Month,Day of month,Week of month,Day of week,Hour,Day/Night";
            bw.write(newline);
            bw.newLine();


            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] content = line.split(cvsSplitBy);

                //parsing data in it normal types

                cmplnt_num = Integer.parseInt(content[0]);
                cmplnt_fr_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[1]);
                cmplnt_fr_tm = new SimpleDateFormat("hh:mm:ss").parse(content[2]);
                cmplnt_to_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[3]);
                cmplnt_to_tm = new SimpleDateFormat("hh:mm:ss").parse(content[4]);
                rpt_dt = new SimpleDateFormat("MM/dd/yyyy").parse(content[5]);
                ky_cd = Integer.parseInt(content[6]);
                ofns_desc = content[7];
                pd_cd = Integer.parseInt(content[8]);
                pd_desc = content[9];
                crm_atpt_cptd_cd = content[10];
                law_cat_cd = content[11];
                juris_desc = content[12];
                boro_nm = content[13];
                addr_pct_cd = Integer.parseInt(content[14]);
                loc_of_occur_desc = content[15];
                prem_typ_desc = content[16];
                parks_nm = content[17];
                hadevelopt = content[18];
                x_coord_cd = Integer.parseInt(content[19]);
                y_coord_cd = Integer.parseInt(content[20]);
                latitude = Double.parseDouble(content[21]);
                longitude = Double.parseDouble(content[22]);
                lat_lon = content[23] + "," + content[24];


                //All changes begin here


                //Initialising Geohash variable
                new_hash = withCharacterPrecision( latitude,  longitude, 12);

                //Hash Time variables initialising
                Date cmplnt_fr= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(content[1]+" "+content[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cmplnt_fr);
                month = calendar.get(Calendar.MONTH);
                hour = calendar.get(Calendar.HOUR);
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                weekOfYear= calendar.get(Calendar.WEEK_OF_YEAR);
                weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
                dayOFMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if(calendar.get(Calendar.AM_PM) == Calendar.PM)
                {day_night="PM";}
                else
                {day_night="AM";}



                System.out.println("[CMPLNT_NUM= " + cmplnt_num + " , CMPLNT_FR_DT=" + cmplnt_fr_dt + " , CMPLNT_FR_TM=" + cmplnt_fr_tm +
                        " , CMPLNT_TO_DT=" + cmplnt_to_dt + " , CMPLNT_TO_TM=" + cmplnt_to_tm + " , RPT_DT=" + rpt_dt + " , KY_CD=" + ky_cd +
                        " , OFNS_DESC=" + ofns_desc + " , PD_CD=" + pd_cd + " , PD_DESC=" + pd_desc + " , CRM_ATPT_CPTD_CD=" + crm_atpt_cptd_cd +
                        " , LAW_CAT_CD=" + law_cat_cd + " , JURIS_DESC=" + juris_desc + " , BORO_NM=" + boro_nm + " , ADDR_PCT_CD=" + addr_pct_cd +
                        " , LOC_OF_OCCUR_DESC=" + loc_of_occur_desc + " , PREM_TYP_DESC=" + prem_typ_desc + " , PARKS_NM=" + parks_nm +
                        " , HADEVELOPT=" + hadevelopt + " , X_COORD_CD=" + x_coord_cd + " , Y_COORD_CD=" + y_coord_cd + " , Latitude=" + latitude +
                        " , Longitude=" + longitude + " , Lat_Lon=" + lat_lon +"  , GeoHash=" + new_hash.toBase32() +
                        ", Week of year= "+weekOfYear+",Month= "+month+ ", Day of month= "+dayOFMonth+",Week of month= "+weekOfMonth+",Day of week= "+dayOfWeek+
                        ",Hour= "+hour+ ",Day/Night= "+day_night+"]");

                newline=line+","+new_hash.toBase32()+","+weekOfYear+","+month+","+dayOFMonth+","+weekOfMonth+","+dayOfWeek+","+hour+ ","+day_night;



                bw.write(newline);
                bw.newLine();
            }
            br.close();
            bw.close();



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }







//        GeoHash geoHash1 = withBitPrecision(48.2119d, 11.3401d, 32);
//        GeoHash geoHash2 = withBitPrecision(48.2437d, 11.4312d, 32);
//        System.out.println(Integer.toBinaryString(geoHash1.intValue()));
//        System.out.println(Integer.toBinaryString(geoHash2.intValue()));
//        System.out.println(Integer.toBinaryString(getBoundaryBoxComparistionValue(geoHash1.intValue(), geoHash2.intValue())));

//        int from = -1827780956;
//        int to = 2074707281;
//        int search = -960776553;
//
//        System.out.println(Integer.toBinaryString(from));
//        System.out.println(Integer.toBinaryString(to));
//        System.out.println(Integer.toBinaryString(search));
//        System.out.println(Integer.toBinaryString(from >> 6));
//        System.out.println(Integer.toBinaryString(to >> 6));
//        System.out.println(Integer.toBinaryString(search >> 6));
////        System.out.println(Integer.toBinaryString(from1));
////        System.out.println(Integer.toBinaryString(from1 >> 6));
//        System.out.println(from >> 6);
//        System.out.println(to >> 6);
//        System.out.println(search >> 6);

  //      GeoHash geoHash = withUnprecisionOfDistanceInMeter(48.2119d, 11.3401d, 10000);
  //      System.out.println(geoHash.significantBits());
  //      System.out.println(geoHash.toBinaryString());



    private static final long serialVersionUID = -8553214249630252175L;
    private static final int[] BITS = {16, 8, 4, 2, 1};
    private static final int BASE32_BITS = 5;
    public static final long FIRST_BIT_FLAGGED = 0x8000000000000000l;
    private static final char[] base32 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private final static Map<Character, Integer> decodeMap = new HashMap<Character, Integer>();

    static {
        int sz = base32.length;
        for (int i = 0; i < sz; i++) {
            decodeMap.put(base32[i], i);
        }
    }

    protected long bits = 0;
    private WGS84Point point;

    private BoundingBox boundingBox;

    protected byte significantBits = 0;

    protected GeoHash() {
    }


    public static int getBitsToShift(int b1, int b2) {
        int max = 32;
        for (int i = 0; i < max; i++) {
            if(b1 == b2) {
                return i;
            }
            b1 = b1 >>> 1;
            b2 = b2 >>> 1;
        }
        return 0;
    }


    public static GeoHash withUnprecisionOfDistanceInMeter(double centerLat, double centerLong, double distanceInMeter) {
        int prec = 32;
        double distLat = centerLat;
        double distLong = centerLong;
        GeoHash geoHash = withBitPrecision(centerLat, centerLong, prec);
        double distInMeter = distFromInMeter(centerLat, centerLong, distLat, distLong);
        while(distInMeter < distanceInMeter) {
            double latitudeSize = geoHash.getBoundingBox().getLatitudeSize();
            distLat += latitudeSize;
            distInMeter = distFromInMeter(centerLat, centerLong, distLat, distLong);
            prec = prec - 2;
            geoHash = withBitPrecision(centerLat, centerLong, prec);
        }
        return geoHash;
    }
    /**
     * This method uses the given number of characters as the desired precision
     * value. The hash can only be 64bits long, thus a maximum precision of 12
     * characters can be achieved.
     *
     * @param latitude lat
     * @param longitude long
     * @param numberOfCharacters number of chars
     *
     * @return geo hash
     */
    public static GeoHash withCharacterPrecision(double latitude, double longitude, int numberOfCharacters) {
        int desiredPrecision = (numberOfCharacters * 5 <= 60) ? numberOfCharacters * 5 : 60;
        return new GeoHash(latitude, longitude, desiredPrecision);
    }

    public static GeoHash withBitPrecision(double latitude, double longitude, int numberOfBits) {
        if (Math.abs(latitude) > 90.0 || Math.abs(longitude) > 180.0) {
            throw new IllegalArgumentException("Can't have lat/lon values out of (-90,90)/(-180/180)");
        }
        return new GeoHash(latitude, longitude, numberOfBits);
    }

    public static GeoHash fromBinaryString(String binaryString) {
        GeoHash geohash = new GeoHash();
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                geohash.addOnBitToEnd();
            } else if (binaryString.charAt(i) == '0') {
                geohash.addOffBitToEnd();
            } else {
                throw new IllegalArgumentException(binaryString + " is not a valid geohash as a binary string");
            }
        }
        geohash.bits <<= (64 - geohash.significantBits);
        long[] latitudeBits = geohash.getRightAlignedLatitudeBits();
        long[] longitudeBits = geohash.getRightAlignedLongitudeBits();
        return geohash.recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    /**
     * build a new {@link GeoHash} from a base32-encoded {@link String}.<br>
     * This will also set up the hashes bounding box and other values, so it can
     * also be used with functions like within().
     *
     * @param geohash geo hash
     *
     * @return geo hash
     */
    public static GeoHash fromGeohashString(String geohash) {
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        boolean isEvenBit = true;
        GeoHash hash = new GeoHash();

        for (int i = 0; i < geohash.length(); i++) {
            int cd = decodeMap.get(geohash.charAt(i));
            for (int j = 0; j < BASE32_BITS; j++) {
                int mask = BITS[j];
                if (isEvenBit) {
                    divideRangeDecode(hash, longitudeRange, (cd & mask) != 0);
                } else {
                    divideRangeDecode(hash, latitudeRange, (cd & mask) != 0);
                }
                isEvenBit = !isEvenBit;
            }
        }

        double latitude = (latitudeRange[0] + latitudeRange[1]) / 2;
        double longitude = (longitudeRange[0] + longitudeRange[1]) / 2;

        hash.point = new WGS84Point(latitude, longitude);
        setBoundingBox(hash, latitudeRange, longitudeRange);
        hash.bits <<= (64 - hash.significantBits);
        return hash;
    }

    public static GeoHash fromIntValue(int hashVal) {
        long val = hashVal;
        val = val << 32;
        return fromLongValue(val, 32);
    }

    public static GeoHash fromLongValue(long hashVal, int significantBits) {
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        boolean isEvenBit = true;
        GeoHash hash = new GeoHash();

        String binaryString = Long.toBinaryString(hashVal);
        while (binaryString.length() < 64) {
            binaryString = "0" + binaryString;
        }
        for (int j = 0; j < significantBits; j++) {
            if (isEvenBit) {
                divideRangeDecode(hash, longitudeRange, binaryString.charAt(j) != '0');
            } else {
                divideRangeDecode(hash, latitudeRange, binaryString.charAt(j) != '0');
            }
            isEvenBit = !isEvenBit;
        }

        double latitude = (latitudeRange[0] + latitudeRange[1]) / 2;
        double longitude = (longitudeRange[0] + longitudeRange[1]) / 2;

        hash.point = new WGS84Point(latitude, longitude);
        setBoundingBox(hash, latitudeRange, longitudeRange);
        hash.bits <<= (64 - hash.significantBits);
        return hash;
    }

    private GeoHash(double latitude, double longitude, int desiredPrecision) {
        point = new WGS84Point(latitude, longitude);
        desiredPrecision = Math.min(desiredPrecision, 64);

        boolean isEvenBit = true;
        double[] latitudeRange = {-90, 90};
        double[] longitudeRange = {-180, 180};

        while (significantBits < desiredPrecision) {
            if (isEvenBit) {
                divideRangeEncode(longitude, longitudeRange);
            } else {
                divideRangeEncode(latitude, latitudeRange);
            }
            isEvenBit = !isEvenBit;
        }

        setBoundingBox(this, latitudeRange, longitudeRange);
        bits <<= (64 - desiredPrecision);
    }

    private static void setBoundingBox(GeoHash hash, double[] latitudeRange, double[] longitudeRange) {
        hash.boundingBox = new BoundingBox(new WGS84Point(latitudeRange[0], longitudeRange[0]), new WGS84Point(
                latitudeRange[1],
                longitudeRange[1]));
    }

    public GeoHash next(int step) {
        return fromOrd(ord() + step, significantBits);
    }

    public GeoHash next() {
        return next(1);
    }

    public GeoHash prev() {
        return next(-1);
    }

    public long ord() {
        int insignificantBits = 64 - significantBits;
        return bits >> insignificantBits;
    }

    public static GeoHash fromOrd(long ord, int significantBits) {
        int insignificantBits = 64 - significantBits;
        return fromLongValue(ord << insignificantBits, significantBits);
    }

    /**
     * Counts the number of geohashes contained between the two (ie how many
     * times next() is called to increment from one to two) This value depends
     * on the number of significant bits.
     *
     * @param one hash one
     * @param two hash two
     * @return number of steps
     */
    public static long stepsBetween(GeoHash one, GeoHash two) {
        if (one.significantBits() != two.significantBits()) {
            throw new IllegalArgumentException(
                    "It is only valid to compare the number of steps between two hashes if they have the same number of significant bits");
        }
        return two.ord() - one.ord();
    }

    private void divideRangeEncode(double value, double[] range) {
        double mid = (range[0] + range[1]) / 2;
        if (value >= mid) {
            addOnBitToEnd();
            range[0] = mid;
        } else {
            addOffBitToEnd();
            range[1] = mid;
        }
    }

    private static void divideRangeDecode(GeoHash hash, double[] range, boolean b) {
        double mid = (range[0] + range[1]) / 2;
        if (b) {
            hash.addOnBitToEnd();
            range[0] = mid;
        } else {
            hash.addOffBitToEnd();
            range[1] = mid;
        }
    }

    /**
     * returns the 8 adjacent hashes for this one. They are in the following
     * order:<br>
     * N, NE, E, SE, S, SW, W, NW
     *
     * @return geo hashes
     */
    public GeoHash[] getAdjacent() {
        GeoHash northern = getNorthernNeighbour();
        GeoHash eastern = getEasternNeighbour();
        GeoHash southern = getSouthernNeighbour();
        GeoHash western = getWesternNeighbour();
        return new GeoHash[]{northern, northern.getEasternNeighbour(), eastern, southern.getEasternNeighbour(),
                southern,
                southern.getWesternNeighbour(), western, northern.getWesternNeighbour()};
    }

    /**
     * how many significant bits are there in this {@link GeoHash}?
     *
     * @return siginificant bits
     */
    public int significantBits() {
        return significantBits;
    }

    public long longValue() {
        return bits;
    }

    public int intValue() {
        long l = longValue() >>> 32;
        return (int)l;
    }

    /**
     * get the base32 string for this {@link GeoHash}.<br>
     * this method only makes sense, if this hash has a multiple of 5
     * significant bits.
     *
     * @return base32
     */
    public String toBase32() {
        if (significantBits % 5 != 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();

        long firstFiveBitsMask = 0xf800000000000000l;
        long bitsCopy = bits;
        int partialChunks = (int) Math.ceil(((double) significantBits / 5));

        for (int i = 0; i < partialChunks; i++) {
            int pointer = (int) ((bitsCopy & firstFiveBitsMask) >>> 59);
            buf.append(base32[pointer]);
            bitsCopy <<= 5;
        }
        return buf.toString();
    }

    /**
     * returns true iff this is within the given geohash bounding box.
     *
     * @param boundingBox bounding box defined by geo hash
     * @return if within
     */
    public boolean within(GeoHash boundingBox) {
        return (bits & boundingBox.mask()) == boundingBox.bits;
    }

    /**
     * find out if the given point lies within this hashes bounding box.<br>
     * <i>Note: this operation checks the bounding boxes coordinates, i.e. does
     * not use the {@link GeoHash}s special abilities.s</i>
     *
     * @param point point
     * @return if contains
     */
    public boolean contains(WGS84Point point) {
        return boundingBox.contains(point);
    }

    /**
     * returns the {@link WGS84Point} that was originally used to set up this.<br>
     * If it was built from a base32-{@link String}, this is the center point of
     * the bounding box.
     *
     * @return point
     */
    public WGS84Point getPoint() {
        return point;
    }

    /**
     * return the center of this {@link GeoHash}s bounding box. this is rarely
     * the same point that was used to build the hash.
     *
     * @return point
     */
    public WGS84Point getBoundingBoxCenterPoint() {
        return boundingBox.getCenterPoint();
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean enclosesCircleAroundPoint(WGS84Point point, double radius) {
        return false;
    }

    protected GeoHash recombineLatLonBitsToHash(long[] latBits, long[] lonBits) {
        GeoHash hash = new GeoHash();
        boolean isEvenBit = false;
        latBits[0] <<= (64 - latBits[1]);
        lonBits[0] <<= (64 - lonBits[1]);
        double[] latitudeRange = {-90.0, 90.0};
        double[] longitudeRange = {-180.0, 180.0};

        for (int i = 0; i < latBits[1] + lonBits[1]; i++) {
            if (isEvenBit) {
                divideRangeDecode(hash, latitudeRange, (latBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                latBits[0] <<= 1;
            } else {
                divideRangeDecode(hash, longitudeRange, (lonBits[0] & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED);
                lonBits[0] <<= 1;
            }
            isEvenBit = !isEvenBit;
        }
        hash.bits <<= (64 - hash.significantBits);
        setBoundingBox(hash, latitudeRange, longitudeRange);
        return hash;
    }

    public GeoHash getNorthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] += 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getSouthernNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        latitudeBits[0] -= 1;
        latitudeBits[0] = maskLastNBits(latitudeBits[0], latitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getEasternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] += 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    public GeoHash getWesternNeighbour() {
        long[] latitudeBits = getRightAlignedLatitudeBits();
        long[] longitudeBits = getRightAlignedLongitudeBits();
        longitudeBits[0] -= 1;
        longitudeBits[0] = maskLastNBits(longitudeBits[0], longitudeBits[1]);
        return recombineLatLonBitsToHash(latitudeBits, longitudeBits);
    }

    protected long[] getRightAlignedLatitudeBits() {
        long copyOfBits = bits << 1;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[0]);
        return new long[]{value, getNumberOfLatLonBits()[0]};
    }

    protected long[] getRightAlignedLongitudeBits() {
        long copyOfBits = bits;
        long value = extractEverySecondBit(copyOfBits, getNumberOfLatLonBits()[1]);
        return new long[]{value, getNumberOfLatLonBits()[1]};
    }

    private long extractEverySecondBit(long copyOfBits, int numberOfBits) {
        long value = 0;
        for (int i = 0; i < numberOfBits; i++) {
            if ((copyOfBits & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
                value |= 0x1;
            }
            value <<= 1;
            copyOfBits <<= 2;
        }
        value >>>= 1;
        return value;
    }

    protected int[] getNumberOfLatLonBits() {
        if (significantBits % 2 == 0) {
            return new int[]{significantBits / 2, significantBits / 2};
        } else {
            return new int[]{significantBits / 2, significantBits / 2 + 1};
        }
    }

    protected final void addOnBitToEnd() {
        significantBits++;
        bits <<= 1;
        bits = bits | 0x1;
    }

    protected final void addOffBitToEnd() {
        significantBits++;
        bits <<= 1;
    }

    @Override
    public String toString() {
        if (significantBits % 5 == 0) {
            return String.format("%s -> %s -> %s", Long.toBinaryString(bits), boundingBox, toBase32());
        } else {
            return String.format("%s -> %s, bits: %d", Long.toBinaryString(bits), boundingBox, significantBits);
        }
    }

    public String toBinaryString() {
        StringBuilder bui = new StringBuilder();
        long bitsCopy = bits;
        for (int i = 0; i < significantBits; i++) {
            if ((bitsCopy & FIRST_BIT_FLAGGED) == FIRST_BIT_FLAGGED) {
                bui.append('1');
            } else {
                bui.append('0');
            }
            bitsCopy <<= 1;
        }
        return bui.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof GeoHash) {
            GeoHash other = (GeoHash) obj;
            if (other.significantBits == significantBits && other.bits == bits) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int f = 17;
        f = 31 * f + (int) (bits ^ (bits >>> 32));
        f = 31 * f + significantBits;
        return f;
    }

    /**
     * return a long mask for this hashes significant bits.
     *
     * @return mask
     */
    private long mask() {
        if (significantBits == 0) {
            return 0;
        } else {
            long value = FIRST_BIT_FLAGGED;
            value >>= (significantBits - 1);
            return value;
        }
    }

    private long maskLastNBits(long value, long n) {
        long mask = 0xffffffffffffffffl;
        mask >>>= (64 - n);
        return value & mask;
    }

    @Override
    public int compareTo(GeoHash o) {
        return Long.valueOf(bits).compareTo(o.bits);
    }


    public static double distFromInMeter(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        double meterConversion = 1609;
        return dist * meterConversion;
    }
}