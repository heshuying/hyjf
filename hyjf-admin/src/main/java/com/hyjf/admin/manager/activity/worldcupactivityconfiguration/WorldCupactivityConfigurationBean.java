package com.hyjf.admin.manager.activity.worldcupactivityconfiguration;

import com.hyjf.mybatis.model.customize.worldcup.WorldTeamCustomize;

import java.io.Serializable;

/**
 * @author xiehuili on 2018/6/13.
 */
public class WorldCupactivityConfigurationBean implements Serializable {

    //1c球队2d球队
    private WorldTeamCustomize team1c2d;
    //1a球队//2b球队
    private WorldTeamCustomize team1a2b;
    //1b球队 //2a球队
    private WorldTeamCustomize team1b2a;
    //1d球队//2c球队
    private WorldTeamCustomize team1d2c;
    //1e球队2f球队
    private WorldTeamCustomize team1e2f;
    //1g球队2h
    private WorldTeamCustomize team1g2h;
    //1f球队//2e球队
    private WorldTeamCustomize team1f2e;
    //1h球队//2g球队
    private WorldTeamCustomize team1h2g;

    public WorldTeamCustomize getTeam1c2d() {
        return team1c2d;
    }

    public void setTeam1c2d(WorldTeamCustomize team1c2d) {
        this.team1c2d = team1c2d;
    }

    public WorldTeamCustomize getTeam1a2b() {
        return team1a2b;
    }

    public void setTeam1a2b(WorldTeamCustomize team1a2b) {
        this.team1a2b = team1a2b;
    }

    public WorldTeamCustomize getTeam1b2a() {
        return team1b2a;
    }

    public void setTeam1b2a(WorldTeamCustomize team1b2a) {
        this.team1b2a = team1b2a;
    }

    public WorldTeamCustomize getTeam1d2c() {
        return team1d2c;
    }

    public void setTeam1d2c(WorldTeamCustomize team1d2c) {
        this.team1d2c = team1d2c;
    }

    public WorldTeamCustomize getTeam1e2f() {
        return team1e2f;
    }

    public void setTeam1e2f(WorldTeamCustomize team1e2f) {
        this.team1e2f = team1e2f;
    }

    public WorldTeamCustomize getTeam1g2h() {
        return team1g2h;
    }

    public void setTeam1g2h(WorldTeamCustomize team1g2h) {
        this.team1g2h = team1g2h;
    }

    public WorldTeamCustomize getTeam1f2e() {
        return team1f2e;
    }

    public void setTeam1f2e(WorldTeamCustomize team1f2e) {
        this.team1f2e = team1f2e;
    }

    public WorldTeamCustomize getTeam1h2g() {
        return team1h2g;
    }

    public void setTeam1h2g(WorldTeamCustomize team1h2g) {
        this.team1h2g = team1h2g;
    }



}
