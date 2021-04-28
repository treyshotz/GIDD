import { useComments, useCreateComment } from 'hooks/Comments';
import { useForm, SubmitHandler } from 'react-hook-form';
import { useSnackbar } from 'hooks/Snackbar';
import { useUser } from 'hooks/User';

// Material UI Components
import { makeStyles } from '@material-ui/core/styles';
import { Typography, Avatar, Divider, InputBase, IconButton, Paper as MuiPaper, Hidden, SwipeableDrawer, Fab, InputBaseProps } from '@material-ui/core';
import SendRoundedIcon from '@material-ui/icons/SendRounded';
import ChatRoundedIcon from '@material-ui/icons/ChatRounded';

// Project Components
import Paper from 'components/layout/Paper';
import { useState, useMemo, forwardRef } from 'react';
import Pagination from 'components/layout/Pagination';
import CommentCard from 'components/miscellaneous/CommentCard';
import NotFoundIndicator from 'components/miscellaneous/NotFoundIndicator';
import { Comment } from 'types/Types';

const useStyles = makeStyles((theme) => ({
  title: {
    fontSize: '1.5rem',
  },
  addComment: {
    display: 'grid',
    gridTemplateColumns: 'auto 1fr',
    gap: theme.spacing(1),
    alignItems: 'center',
    paddingTop: theme.spacing(1),
    [theme.breakpoints.down('md')]: {
      padding: theme.spacing(1),
    },
  },
  divider: {
    margin: theme.spacing(2, 1),
  },
  paperRoot: {
    padding: theme.spacing(0.25, 0.5),
    display: 'flex',
    backgroundColor: 'transparent',
    alignItems: 'center',
  },
  input: {
    marginLeft: theme.spacing(1),
    width: '100%',
  },
  iconButton: {
    padding: theme.spacing(1),
  },
  commentsPaper: {
    borderRadius: `${theme.shape.borderRadius}px ${theme.shape.borderRadius}px 0 0`,
    background: theme.palette.background.paper,
    maxHeight: '65vh',
  },
  commentBtn: {
    position: 'fixed',
    bottom: theme.spacing(2),
    left: theme.spacing(2),
  },
  heading: {
    paddingTop: theme.spacing(2),
    paddingBottom: theme.spacing(2),
  },
  list: {
    display: 'grid',
    gap: theme.spacing(1),
  },
  comments: {
    overflow: 'auto',
    [theme.breakpoints.down('md')]: {
      padding: theme.spacing(1),
    },
  },
}));

type FormValues = Pick<Comment, 'comment'>;

export type CommentsProps = { activityId: string };

const Comments = (props: CommentsProps) => {
  const classes = useStyles();
  const { data: user } = useUser();
  const [viewComments, setViewComments] = useState(false);
  const { data, error, hasNextPage, fetchNextPage, isFetching } = useComments(props.activityId, { sort: 'createdAt,DESC' });
  const comments = useMemo(
    () =>
      data !== undefined
        ? data.pages
            .map((page) => page.content)
            .flat(1)
            .sort((a, b) => b.createdAt.localeCompare(a.createdAt))
        : [],
    [data],
  );
  const isEmpty = useMemo(() => !comments.length && !isFetching, [comments, isFetching]);
  const { handleSubmit, register, reset } = useForm<FormValues>();
  const createComment = useCreateComment(props.activityId);
  const showSnackbar = useSnackbar();

  const submit: SubmitHandler<FormValues> = async (data) => {
    createComment.mutate(data, {
      onSuccess: () => {
        reset();
      },
      onError: (e) => {
        showSnackbar(e.message, 'error');
      },
    });
  };

  const Input = forwardRef(({ ...props }: InputBaseProps, ref) => (
    <InputBase {...props} className={classes.input} inputRef={ref} multiline name={props.name} placeholder='Skriv din kommentar her...' />
  ));
  Input.displayName = 'Input';

  const AddComment = () => (
    <div className={classes.addComment}>
      <Avatar src={user?.image} />
      <MuiPaper className={classes.paperRoot} component='form' onSubmit={handleSubmit(submit)}>
        <Input {...register('comment')} />
        <IconButton aria-label='send' className={classes.iconButton} color='primary' type='submit'>
          <SendRoundedIcon />
        </IconButton>
      </MuiPaper>
    </div>
  );
  const CommentSection = () => (
    <Pagination fullWidth hasNextPage={hasNextPage} isLoading={isFetching} nextPage={() => fetchNextPage()}>
      {isEmpty && <NotFoundIndicator header={error?.message || 'Fant ingen kommentarer'} />}
      <div className={classes.list}>
        {comments.map((comment) => (
          <CommentCard activityId={props.activityId} comment={comment} key={comment.id} />
        ))}
      </div>
    </Pagination>
  );

  return (
    <>
      <Hidden mdUp>
        <Fab aria-label='add' className={classes.commentBtn} color='primary' onClick={() => setViewComments(true)}>
          <ChatRoundedIcon />
        </Fab>
        <SwipeableDrawer
          anchor='bottom'
          classes={{ paper: classes.commentsPaper }}
          disableSwipeToOpen
          onClose={() => setViewComments(false)}
          onOpen={() => setViewComments(true)}
          open={viewComments}
          swipeAreaWidth={56}>
          <div className={classes.comments}>
            <Typography className={classes.title} gutterBottom variant='h2'>
              Kommentarer
            </Typography>
            <CommentSection />
          </div>
          <AddComment />
        </SwipeableDrawer>
      </Hidden>
      <Hidden mdDown>
        <Paper>
          <Typography className={classes.title} variant='h2'>
            Kommentarer
          </Typography>
          <AddComment />
          <Divider className={classes.divider} variant='middle' />
          <CommentSection />
        </Paper>
      </Hidden>
    </>
  );
};

export default Comments;
