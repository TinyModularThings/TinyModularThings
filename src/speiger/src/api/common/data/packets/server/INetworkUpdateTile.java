package speiger.src.api.common.data.packets.server;

public interface INetworkUpdateTile extends INetworkTile
{
	public void onSync(String field);
}
