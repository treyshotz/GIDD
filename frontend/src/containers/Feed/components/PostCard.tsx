import { useState } from 'react';
import classnames from 'classnames';
import { Link } from 'react-router-dom';
import { Post } from 'types/Types';
import { getTimeSince } from 'utils';
import { parseISO } from 'date-fns';
import URLS from 'URLS';
import { useUser } from 'hooks/User';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Avatar, List, ListItem, ListItemAvatar, ListItemText, ListItemSecondaryAction, IconButton } from '@material-ui/core';

// Icons
import MoreIcon from '@material-ui/icons/MoreVertRounded';

// Project Components
import Paper from 'components/layout/Paper';
import ActivityCard from 'components/layout/ActivityCard';
import AspectRatioImg from 'components/miscellaneous/AspectRatioImg';
import PostCreateCard from 'containers/Feed/components/PostCreateCard';

const useStyles = makeStyles((theme) => ({
  grid: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  paper: {
    padding: theme.spacing(1),
  },
  creator: {
    padding: theme.spacing(0, 1),
  },
  img: {
    borderRadius: theme.shape.borderRadius,
  },
  menu: {
    right: 0,
  },
}));

export type PostCardProps = {
  post: Post;
  preview?: boolean;
};

const PostCard = ({ post, preview = false }: PostCardProps) => {
  const classes = useStyles();
  const [openEdit, setOpenEdit] = useState(false);
  const { data: user } = useUser();

  return (
    <Paper className={classnames(classes.grid, classes.paper)}>
      <List disablePadding>
        <ListItem className={classes.creator} component='div'>
          <ListItemAvatar>
            <Link to={`${URLS.USERS}${post.creator.id}/`}>
              <Avatar src={post.creator.image} />
            </Link>
          </ListItemAvatar>
          <ListItemText
            primary={<Link to={`${URLS.USERS}${post.creator.id}/`}>{`${post.creator.firstName} ${post.creator.surname}`}</Link>}
            secondary={`${getTimeSince(parseISO(post.createdAt))}`}
          />
          {user?.id === post.creator.id && !preview && (
            <ListItemSecondaryAction className={classes.menu}>
              <PostCreateCard onClose={() => setOpenEdit(false)} open={openEdit} post={post} />
              <IconButton onClick={() => setOpenEdit(true)}>
                <MoreIcon />
              </IconButton>
            </ListItemSecondaryAction>
          )}
        </ListItem>
      </List>

      {Boolean(post.content) && <Typography>{post.content}</Typography>}
      {post.image && <AspectRatioImg alt='Bilde' imgClassName={classes.img} src={post.image} />}
      {post.activity && <ActivityCard activity={post.activity} />}
      <Paper noPadding>{`${post.likesCount} liker - ${post.commentsCount} kommentarer`}</Paper>
    </Paper>
  );
};

export default PostCard;
