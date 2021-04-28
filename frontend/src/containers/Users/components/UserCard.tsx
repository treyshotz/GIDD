import URLS from 'URLS';
import { Link } from 'react-router-dom';
import { UserList } from 'types/Types';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Avatar, ListItem, ListItemText, ListItemAvatar } from '@material-ui/core';

// Project components
import Paper from 'components/layout/Paper';

// Icon
import OpenIcon from '@material-ui/icons/ArrowForwardRounded';

const useStyles = makeStyles(() => ({
  secondaryText: {
    whiteSpace: 'break-spaces',
  },
}));

export type UserCardProps = {
  user: UserList;
};

const UserCard = ({ user }: UserCardProps) => {
  const classes = useStyles();
  return (
    <Paper noPadding>
      <ListItem button component={Link} to={`${URLS.USERS}${user.id}/`}>
        <ListItemAvatar>
          <Avatar src={user.image} />
        </ListItemAvatar>
        <ListItemText classes={{ secondary: classes.secondaryText }} primary={`${user.firstName} ${user.surname}`} secondary={`${user.email}`} />
        <OpenIcon />
      </ListItem>
    </Paper>
  );
};

export default UserCard;
