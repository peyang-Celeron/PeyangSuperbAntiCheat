package ml.peya.plugins.Moderate;

import ml.peya.plugins.Utils.SQL;
import ml.peya.plugins.Utils.Utils;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static ml.peya.plugins.Variables.banKick;

/**
 * Bansのメソッド群。
 */
public class BanAnalyzer
{
    /**
     * プレイヤーのBAN履歴の取得
     *
     * @param uuid UUID。
     * @param type 取得タイプ(BAN, KICK, MUTE, *ALL*)
     * @return BANデータ
     */
    public static ArrayList<Bans> getAbuse(UUID uuid, Type type)
    {
        ArrayList<Bans> abuses = new ArrayList<>();

        switch (type)
        {
            case ALL:
            case KICK:
                try (Connection connection = banKick.getConnection();
                     PreparedStatement statement = connection.prepareStatement("SELECT DATE, REASON, PLAYER, UUID, KICKID FROM kick WHERE UUID=?"))
                {
                    statement.setString(1, uuid.toString().replace("-", ""));
                    ResultSet set = statement.executeQuery();
                    while (set.next())
                    {
                        abuses.add(new Bans(
                                set.getLong("DATE"),
                                set.getString("REASON"),
                                set.getString("PLAYER"),
                                set.getString("UUID"),
                                set.getString("KICKID").replace("#", ""),
                                Type.KICK
                        ));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Utils.errorNotification(Utils.getStackTrace(e));
                }
                if (type == Type.KICK)
                    break;
            case BAN:
                try (Connection connection = banKick.getConnection();
                     PreparedStatement statement = connection.prepareStatement("SELECT DATE, REASON, PLAYER, UUID, BANID FROM ban WHERE UUID=?"))
                {
                    statement.setString(1, uuid.toString().replace("-", ""));
                    ResultSet set = statement.executeQuery();
                    while (set.next())
                    {
                        abuses.add(new Bans(
                                set.getLong("DATE"),
                                set.getString("REASON"),
                                set.getString("PLAYER"),
                                set.getString("UUID"),
                                set.getString("BANID").replace("#", ""),
                                Type.BAN
                        ));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Utils.errorNotification(Utils.getStackTrace(e));
                }
                if (type == Type.BAN)
                    break;
            default:
                return abuses;
        }

        return abuses;
    }

    /**
     * Ban情報を<b>登録</b>します。
     * 実際にはBANせず、登録するだけです。
     *
     * @param reason Ban理由
     * @param player Banプレイヤー
     */
    public static void ban(Player player, String reason)
    {
        StringBuilder id = new StringBuilder();
        Random random = new Random();

        IntStream.range(0, 8).parallel().forEachOrdered(i ->
                id.append(random.nextBoolean() ? random.nextInt(9): (char) (random.nextInt(5) + 'A')));

        try (Connection connection = banKick.getConnection())
        {
            SQL.insert(connection, "ban",
                    player.getName(),
                    player.getUniqueId().toString().replace("-", ""),
                    id.toString(),
                    new Date().getTime(),
                    reason,
                    1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Utils.errorNotification(Utils.getStackTrace(e));
        }
    }

    /**
     * BAN情報取得タイプ
     */
    public enum Type
    {
        /**
         * すべて
         */
        ALL(null),
        /**
         * KICKのみ
         */
        KICK("Kick"),
        /**
         * BANのみ
         */
        BAN("Ban"),
        /**
         * MUTEのみ
         */
        MUTE("Mute");

        /**
         * text
         */
        String text;

        Type(String text)
        {
            this.text = text;
        }

        /**
         * StringをTypeに変換する。
         *
         * @param name before
         * @return AFTER
         */
        public static Type toType(String name)
        {
            switch (name.toLowerCase())
            {
                case "kick":
                    return KICK;
                case "ban":
                    return BAN;
                case "mute":
                    return MUTE;
                case "all":
                default:
                    return ALL;
            }
        }

        /**
         * Textを取得
         * @return てきすと
         */
        public String text()
        {
            return this.text;
        }
    }

    /**
     * またタイプだわーいDoc(ry
     */
    public static class Bans
    {
        private final long date;
        private final String reason;
        private final String playerId;
        private final String uuId;
        private final String id;
        private final Type type;

        /**
         * コンストラクタで組み立てる。
         *
         * @param date     UNIX時間。
         * @param reason   判定タイプ。
         * @param playerId 管理ID？
         * @param uuId     UUID。
         * @param id       管理ID。
         * @param type     処罰方法。
         */
        public Bans(long date, String reason, String playerId, String uuId, String id, Type type)
        {
            this.date = date;
            this.reason = reason;
            this.id = id;
            this.playerId = playerId;
            this.uuId = uuId;
            this.type = type;
        }

        /**
         * UNIX時間のゲッター。
         *
         * @return UNIX時間。
         */
        public long getDate()
        {
            return date;
        }

        /**
         * 判定タイプのゲッター。
         *
         * @return 判定タイプ。
         */
        public String getReason()
        {
            return reason;
        }

        /**
         * 管理IDのゲッター。
         *
         * @return 管理ID。
         */
        public String getId()
        {
            return id;
        }

        /**
         * 管理ID？のゲッター。
         *
         * @return 管理ID？
         */
        public String getPlayerId()
        {
            return playerId;
        }

        /**
         * UUIDのゲッター。
         *
         * @return UUID。
         */
        public String getUuId()
        {
            return uuId;
        }

        /**
         * 処罰方法のゲッター。
         *
         * @return 処罰方法。
         */
        public Type getType()
        {
            return type;
        }
    }

}

