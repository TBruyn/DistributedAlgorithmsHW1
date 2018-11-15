import java.util.Arrays;

public class Timestamp implements Comparable<Timestamp> {
    private final int[] time;

    public Timestamp(int[] time) {
        this.time = time;
    }

    public int[] getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timestamp timestamp = (Timestamp) o;
        return Arrays.equals(getTime(), timestamp.getTime());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getTime());
    }

    @Override
    public int compareTo(Timestamp other) {
        int[] otherTime = other.getTime();
        boolean bigger = this.time[0] > otherTime[0];
        for (int i = 1; i < this.time.length; i++) {
            if ( this.time[i] == otherTime[i]
                    || (this.time[i] > otherTime[i]) != bigger ) return 0;
        }

        return bigger ? 1 : -1;
    }
}
